package net.pingfang.common.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnBean(value = LiquibaseProperties.class)
@Slf4j
public class LiquibaseConfig {
//	public static List<String> database = Lists.newCopyOnWriteArrayList();

	@Resource
	LiquibaseProperties properties;

	@Bean("liquibase")
	public SpringLiquibase liquibase(DataSource dataSource) throws IOException {
//		File temDir = com.google.common.io.Files.createTempDir();
//		List<String> paths = properties.getChangelog();
//		MigrationsUtils migrationsUtils = new MigrationsUtils(temDir);
//		List<String> files = paths.stream().map(migrationsUtils::writeResourceToMigrationDirectory)
//				.flatMap(Collection::stream).collect(Collectors.toList());
//		String str = MigrationsUtils.generate(files);
//		File file = new File(temDir, "migrations.xml");
//		// 获取文件
//		Files.write(str.getBytes(StandardCharsets.UTF_8), file);
//		log.info("Dynamic Migration File: {}", file.getAbsolutePath());
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:database/changelog.xml");
		liquibase.setResourceLoader(new DefaultResourceLoader());
//		liquibase.setShouldRun(file.exists());
//		directory(temDir);
		return liquibase;
	}

	/**
	 * 创建临时文本文件
	 *
	 * @param txt 内容
	 * @return
	 * @throws IOException
	 */
	public static File craeteTempFileTxt(String txt) throws IOException {
		File temp = File.createTempFile("data_", ".xml");
		temp.deleteOnExit();
		BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		out.write(txt);
		out.close();
		if (temp.exists()) {
			log.info("文件存在");
			String name = temp.getName();
			String absolutePath = temp.getAbsolutePath();
			log.info("文件名称:" + name);
			log.info("文件路径" + absolutePath);
			return temp;
		} else {
			log.info("文件不存在");
			throw new RemoteException("文件不存在");
		}
	}

	public void directory(File file) {
		// cleanup directory on exit
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try (Stream<Path> dir = java.nio.file.Files.walk(file.toPath())) {
					dir.sorted(Comparator.reverseOrder()) //
							.map(Path::toFile) //
							.forEach(File::delete);
				} catch (IOException e) {
					log.error("Shutdown Cleanup Error", e);
				}
			}
		});
	}
}
