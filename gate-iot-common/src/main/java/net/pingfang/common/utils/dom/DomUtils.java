package net.pingfang.common.utils.dom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-22 15:53
 */
public class DomUtils {
	private static Logger logger = LoggerFactory.getLogger(DomUtils.class);

	/**
	 * 通过文件的路径获取xml的document对象
	 *
	 * @param path 文件的路径
	 * @return 返回文档对象
	 */
	public static Document getXMLByFilePath(String path) {
		if (null == path) {
			return null;
		}
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(new File(path));
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 通过xml字符获取document文档
	 *
	 * @param xmlstr 要序列化的xml字符
	 * @return 返回文档对象
	 * @throws DocumentException
	 */
	public static Document getXMLByString(String xmlstr) throws DocumentException {
		if (xmlstr == "" || xmlstr == null) {
			return null;
		}
		return DocumentHelper.parseText(xmlstr);
	}

	/**
	 * 获取某个元素的所有的子节点
	 *
	 * @param em 制定节点
	 * @return 返回所有的子节点
	 */
	public static List<Element> getChildElements(Element em) {
		if (null == em) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Element> lists = em.elements();
		return lists;
	}

	/**
	 * 获取指定节点的子节点
	 *
	 * @param node      父节点
	 * @param childnode 指定名称的子节点
	 * @return 返回指定的子节点
	 */
	public static Element getChildElement(Element node, String childnode) {
		if (null == node || null == childnode || "".equals(childnode)) {
			return null;
		}
		return node.element(childnode);
	}

	/**
	 * 获取所有的属性值
	 *
	 * @param node
	 * @param arg
	 * @return
	 */
	public static Map<String, String> getAttributes(Element node, String... arg) {
		if (node == null || arg.length == 0) {
			return null;
		}
		Map<String, String> attrMap = new HashMap<String, String>();
		for (String attr : arg) {
			String attrValue = node.attributeValue(attr);
			attrMap.put(attr, attrValue);
		}
		return attrMap;
	}

	/**
	 * 获取element的单个属性
	 *
	 * @param node 需要获取属性的节点对象
	 * @param attr 需要获取的属性值
	 * @return 返回属性的值
	 */
	public static String getAttribute(Element node, String attr) {
		if (null == node || attr == null || "".equals(attr)) {
			return "";
		}
		return node.attributeValue(attr);
	}

	/**
	 * 添加孩子节点元素
	 *
	 * @param parent     父节点
	 * @param childName  孩子节点名称
	 * @param childValue 孩子节点值
	 * @return 新增节点
	 */
	public static Element addChild(Element parent, String childName, String childValue) {
		Element child = parent.addElement(childName);// 添加节点元素
		child.setText(childValue == null ? "" : childValue); // 为元素设值
		return child;
	}

	/**
	 * DOM4j的Document对象转为XML报文串
	 *
	 * @param document
	 * @param charset
	 * @return 经过解析后的xml字符串
	 */
	public static String documentToString(Document document, String charset) {
		StringWriter stringWriter = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();// 获得格式化输出流
		format.setEncoding(charset);// 设置字符集,默认为UTF-8
		XMLWriter xmlWriter = new XMLWriter(stringWriter, format);// 写文件流
		try {
			xmlWriter.write(document);
			xmlWriter.flush();
			xmlWriter.close();
		} catch (IOException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
		return stringWriter.toString();
	}

	/**
	 * 去掉声明头的(即<?xml...?>去掉)
	 *
	 * @param document
	 * @param charset
	 * @return
	 */
	public static String documentToStringNoDeclaredHeader(Document document, String charset) {
		String xml = documentToString(document, charset);
		return xml.replaceFirst("\\s*<[^<>]+>\\s*", "");
	}

	/**
	 * 解析XML为Document对象
	 *
	 * @param xml 被解析的XMl
	 * @return Document
	 */
	public final static Element parseXml(String xml) {
		StringReader sr = new StringReader(xml);
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(sr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element rootElement = document != null ? document.getRootElement() : null;
		return rootElement;
	}

	/**
	 * 获取element对象的text的值
	 *
	 * @param e   节点的对象
	 * @param tag 节点的tag
	 * @return
	 */
	public final static String getText(Element e, String tag) {
		Element _e = e.element(tag);
		if (_e != null)
			return _e.getText();
		else
			return null;
	}

	/**
	 * 获取去除空格的字符串
	 *
	 * @param e
	 * @param tag
	 * @return
	 */
	public final static String getTextTrim(Element e, String tag) {
		Element _e = e.element(tag);
		if (_e != null)
			return _e.getTextTrim();
		else
			return null;
	}

	/**
	 * 获取节点值.节点必须不能为空，否则抛错
	 *
	 * @param parent 父节点
	 * @param tag    想要获取的子节点
	 * @return 返回子节点
	 */
	public final static String getTextTrimNotNull(Element parent, String tag) {
		Element e = parent.element(tag);
		if (e == null)
			throw new NullPointerException("节点为空");
		else
			return e.getTextTrim();
	}

	/**
	 * 节点必须不能为空，否则抛错
	 *
	 * @param parent 父节点
	 * @param tag    想要获取的子节点
	 * @return 子节点
	 */
	public final static Element elementNotNull(Element parent, String tag) {
		Element e = parent.element(tag);
		if (e == null)
			throw new NullPointerException("节点为空");
		else
			return e;
	}

	/**
	 * 将文档对象写入对应的文件中
	 *
	 * @param document 文档对象
	 * @param path     写入文档的路径
	 * @throws IOException
	 */
	public final static void writeXMLToFile(Document document, String path) throws IOException {
		if (document == null || path == null) {
			return;
		}
		XMLWriter writer = new XMLWriter(new FileWriter(path));
		writer.write(document);
		writer.close();
	}

	// 传入xml格式的string，转化为xml类型，然后解析其内容，返回map数据形式
	public static List<Map<String, Object>> strToXmlAndParserXml(String strXml) throws Exception {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new ByteArrayInputStream(strXml.getBytes(StandardCharsets.UTF_8)));
		List<Map<String, Object>> xml = parserXml(doc);
		return xml;
	}

	// 遍历解析xml数据
	public static List<Map<String, Object>> parserXml(Document doc) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Element root = doc.getRootElement(); // 获取根节点
		Iterator<?> it = root.elementIterator();
		Element element;
		Element elements;

		// 解析子节点 entity
		while (it.hasNext()) {
			element = (Element) it.next();
			Iterator<?> its = element.elementIterator();
			// 解析子节点 entity 下的节点
			Map<String, Object> map = new HashMap<String, Object>();
			while (its.hasNext()) {
				elements = (Element) its.next();
				map.put(elements.getName(), elements.getText());
			}
			list.add(map);
		}

		return list;
	}

	// 传入xml格式的string，转化为xml类型，然后解析其内容，返回 实体类 数据形式
	public static <T> List<T> strToXmlAndParserEntity(String strXml, Class<T> clazz) throws Exception {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new ByteArrayInputStream(strXml.getBytes(StandardCharsets.UTF_8)));
		List<T> xml = parserXmlEntity(doc, clazz);
		return xml;
	}

	public static <T> List<T> parserXmlEntity(Document doc, Class<T> clazz) throws Exception {
		List<T> list = new ArrayList<T>();
		Element root = doc.getRootElement(); // 获取根节点
		Iterator<?> it = root.elementIterator();
		Element element;
		Element elements;
		// 解析子节点 entity
		while (it.hasNext()) {
			element = (Element) it.next();
			Iterator<?> its = element.elementIterator();
			// 解析子节点 entity 下的节点
			Map<String, Object> map = new HashMap<String, Object>();

			while (its.hasNext()) {
				elements = (Element) its.next();
				map.put(elements.getName(), elements.getText());
			}
			list.add(mapToEntity(clazz, map));

		}

		return list;
	}

	/**
	 * map转实体类
	 *
	 * @param clazz
	 * @param map
	 * @param <T>
	 * @return
	 */
	public static <T> T mapToEntity(Class<T> clazz, Map<String, Object> map) {
		if (null == map) {
			return null;
		}
		// 取到类下所有的属性，也就是变量名
		Field[] fields = clazz.getDeclaredFields();
		try {
			T entity = clazz.newInstance();
			for (Field field : fields) {
				String fieldName = field.getName();
				if (map.get(fieldName) == null) {
					continue;
				}
				// 把属性的第一个字母处理成大写
				String stringLetter = fieldName.substring(0, 1).toUpperCase();
				// 取得set方法名，比如setBbzt
				String setterName = "set" + stringLetter + fieldName.substring(1);
				// 属性类型
				Class<?> fieldClass = field.getType();
				// 取到类下所有方法
				Method[] methodArray = clazz.getMethods();
				for (Method method : methodArray) {
					// 真正取得set方法。
					String methodName = method.getName();
					if (methodName.equals(setterName)) {
						if (fieldClass == String.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, String.valueOf(map.get(fieldName)));// 为其赋值
						} else if (fieldClass == Integer.class || fieldClass == int.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, Integer.parseInt(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == Boolean.class || fieldClass == boolean.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, Boolean.getBoolean(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == Short.class || fieldClass == short.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, Short.parseShort(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == Long.class || fieldClass == long.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, Long.parseLong(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == Double.class || fieldClass == double.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, Double.parseDouble(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == Float.class || fieldClass == float.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, Float.parseFloat(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == BigInteger.class) {
							method = clazz.getMethod(setterName, fieldClass);
							method.invoke(entity, new BigInteger(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == BigDecimal.class) {
							method = clazz.getMethod(setterName, fieldClass);
							if (map.get(fieldName) == null || StringUtils.isBlank(map.get(fieldName).toString())) {
								map.put(fieldName, "0");
							}
							method.invoke(entity, new BigDecimal(String.valueOf(map.get(fieldName))));// 为其赋值
						} else if (fieldClass == Date.class) {
							method = clazz.getMethod(setterName, fieldClass);
							if (map.get(fieldName).getClass() == java.sql.Date.class) {
								method.invoke(entity, new Date(((java.sql.Date) map.get(fieldName)).getTime()));// 为其赋值
							} else if (map.get(fieldName).getClass() == java.sql.Time.class) {
								method.invoke(entity, new Date(((java.sql.Time) map.get(fieldName)).getTime()));// 为其赋值
							} else if (map.get(fieldName).getClass() == java.sql.Timestamp.class) {
								method.invoke(entity, new Date(((java.sql.Timestamp) map.get(fieldName)).getTime()));// 为其赋值
							}
						}
					}
				}
			}
			return entity;
		} catch (InstantiationException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			logger.error("", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error("实例化 JavaBean 失败", e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("映射错误", e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			logger.error("字段映射失败", e);
		}

		return null;

	}

	/**
	 * 根据 节点名称,解析 xml Attribute数据
	 *
	 * @param xml      xml字符串
	 * @param nodeName 节点名称
	 * @return 所有节点
	 */

	public static List<Map<String, Object>> parserXmlAttribute(String xml, String nodeName) {
		String XMLResponse = xml.substring(xml.indexOf("<XMLResponse>"));
		Element em = parseXml(XMLResponse);
		List<Map<String, Object>> xmlMap = listNodes(em, nodeName);
		return xmlMap;

	}

	/**
	 * 递归解析xml节点，适用于 多节点数据
	 *
	 * @param node     节点
	 * @param nodeName 节点名称
	 * @return 所有节点
	 */
	// 遍历当前节点下的所有节点 ，nodeName 要解析的节点名称
	public static List<Map<String, Object>> listNodes(Element node, String nodeName) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		// 首先获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();

		Map<String, Object> map = null;
		// 遍历属性节点
		for (Attribute attribute : list) {
			if (node.getName().equals(nodeName)) {
				if (null == map) {
					map = new HashMap<String, Object>();
					listMap.add(map);
				}
				// 取到的节点属性放在map中
				map.put(attribute.getName(), attribute.getValue());
			}

		}
		// 如果当前节点内容不为空，则输出
		if (!(node.getTextTrim().equals(""))) {
		}
		// 同时迭代当前节点下面的所有子节点
		// 使用递归
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			listMap.addAll(listNodes(e, nodeName));
		}
		return listMap;
	}

	/**
	 * xml转为字符串
	 *
	 * @param fileUrl 文件地址
	 * @return 字符串
	 */
	public static String xmlToString(String fileUrl) throws IOException {
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(fileUrl);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			reader = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8), 5 * 1024 * 1024);// 用5M的缓冲读取文本文件
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return sb.toString();
	}

}
