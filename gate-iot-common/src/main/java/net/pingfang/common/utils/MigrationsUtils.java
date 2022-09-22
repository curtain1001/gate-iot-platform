package net.pingfang.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MigrationsUtils {
	final File tempDir;

	public MigrationsUtils(File tempDir) {
		this.tempDir = tempDir;
	}

	public static String generate(List<String> changes) {
		String including = changes.stream().map(c -> {
//			c = c.replaceFirst("/database","classpath:");
			return "<include file=\"" + c + "\" relativeToChangelogFile=\"true\" />";
		}).collect(Collectors.joining("\r\n"));
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<databaseChangeLog xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\r\n"
				+ "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "	xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog\r\n"
				+ "         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.3.xsd\">\r\n" + including //
				+ "</databaseChangeLog>";
	}

	/**
	 *
	 * @param relPath
	 * @param resourcePath 目录 /database/module
	 * @param path
	 * @param target
	 * @throws Exception
	 */
	public static void copyAndGenLogicalFilePath(String relPath, String resourcePath, Path path, OutputStream target)
			throws Exception {
		if (relPath.endsWith("xml")) {
			String logicalPath = resourcePath + "/" + relPath;
			Document doc = parseXML(Files.newInputStream(path));
			Element databaseChangeLog = doc.getDocumentElement();
			databaseChangeLog.setAttribute("logicalFilePath", logicalPath);
			String docString = toString(doc);
			ByteStreams.copy(new ByteArrayInputStream(docString.getBytes("UTF-8")), target);
		} else {
			ByteStreams.copy(Files.newInputStream(path), target);
		}
	}

	/**
	 *
	 * @param resourcePath 文件 /schema.xml
	 * @param xmlStream
	 * @param target
	 * @throws Exception
	 */
	public static void copyAndGenLogicalFilePath(String resourcePath, InputStream xmlStream, OutputStream target)
			throws Exception {
		String logicalPath = resourcePath;
		Document doc = parseXML(xmlStream);
		Element databaseChangeLog = doc.getDocumentElement();
		databaseChangeLog.setAttribute("logicalFilePath", logicalPath);
		String docString = toString(doc);
		ByteStreams.copy(new ByteArrayInputStream(docString.getBytes("UTF-8")), target);
	}

	private static String toString(Document newDoc) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter sw = new StringWriter();
		try {
			DOMSource domSource = new DOMSource(newDoc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
			return sw.toString();
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
				sw = null;
			}
		}
	}

	private static Document parseXML(InputStream inputStream)
			throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
		df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
		return df.newDocumentBuilder().parse(inputStream);
	}

	/**
	 * write resource to a new migration directory. if resource is a folder, all the
	 * folder's contents will be copied.
	 *
	 * @param resourcePath
	 * @return list of XML files
	 */
	public List<String> writeResourceToMigrationDirectory(String resourcePath) {
		try {
			List<String> changelogFiles = Lists.newArrayList();
			File target = new File(this.tempDir, resourcePath);
			com.google.common.io.Files.createParentDirs(target);
			boolean isDir = false;
			URL url = this.getClass().getResource(resourcePath);
			URI uri = url != null ? url.toURI() : null;
			if (uri == null) {
				log.error("No Changelog found in resource: {}", resourcePath);
				return Arrays.asList();
			}
			try {
				log.debug("URI: {}", uri);
				Path path = null;
				if ("file".equals(uri.getScheme())) {
					path = Paths.get(uri);
					isDir = checkDirectoryAndCopy(path, target, changelogFiles, resourcePath);
				} else if ("jar".equals(uri.getScheme())) {
					String[] part = uri.toString().split("!");
					try (FileSystem fs = FileSystems.newFileSystem(URI.create(part[0]), Maps.newHashMap())) {
						path = fs.getPath(part[1]);
						isDir = checkDirectoryAndCopy(path, target, changelogFiles, resourcePath);
					}
				} else {
					// no other possible options now.
				}
			} catch (Exception e) {
				log.debug("Resource URI {} not a directory: {}", uri, resourcePath, e);
			}
			if (!isDir) {
				try {
					log.debug("Copying Resource {} to {}", resourcePath, target);
					InputStream ins = this.getClass().getResourceAsStream(resourcePath);
					if (ins != null) {
						if (resourcePath.endsWith(".xml")) {
							MigrationsUtils.copyAndGenLogicalFilePath(resourcePath, ins, new FileOutputStream(target));
							changelogFiles.add(resourcePath);
						} else {
							ByteStreams.copy(ins, new FileOutputStream(target));
						}
					} else {
						log.debug("Resource not found {}", resourcePath);
					}
				} catch (Exception e) {
					log.error("Error Copying Resource: {}", resourcePath, e);
				}
			}
			return changelogFiles;
		} catch (Exception e) {
			throw new RuntimeException("Resource Path: " + resourcePath, e);
		}
	}

	protected boolean checkDirectoryAndCopy(Path path, File target, List<String> changelogFiles, String resourcePath)
			throws Exception {
		log.debug("Path: {}", path);
		if (Files.isDirectory(path)) {
			try (Stream<Path> ps = Files.walk(path)) {
				ps.sorted().forEach(p -> {
					if (!Files.isDirectory(p)) {
						try {
							String relPath = path.relativize(p).toString();
							File targetFile = new File(target, relPath);
							com.google.common.io.Files.createParentDirs(targetFile);
							log.debug("Copying {} -> {}", p, targetFile);
							MigrationsUtils.copyAndGenLogicalFilePath(relPath, resourcePath, p,
									new FileOutputStream(targetFile));
							if (relPath.endsWith(".xml")) {
								changelogFiles.add(targetFile.getPath());
							}
						} catch (Exception e) {
							log.error("Error Copying Files in Directory {}", p, e);
							throw new RuntimeException(e);
						}
					}
				});
			}
			return true;
		} else {
		}
		return false;
	}

}
