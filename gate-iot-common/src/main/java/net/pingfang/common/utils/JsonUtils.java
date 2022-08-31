package net.pingfang.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class JsonUtils {
	public static ObjectMapper MAPPER = createObjectMapper();

	public static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper;
	}

	public static ObjectNode getObjectNode() {
		return MAPPER.createObjectNode();
	}

	/**
	 * 判断是否为json类型
	 *
	 */
	public static boolean isJSONValid(Object jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString.toString());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static String toJsonString(Object value) {
		if (value == null) {
			return null;
		}
		try {
			return MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Convert object to json string error from value:" + value, e);
		}
	}

	public static byte[] toJsonBytes(Object value) {
		if (value == null) {
			return null;
		}
		try {
			return MAPPER.writeValueAsBytes(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Convert object to json string error from value:" + value, e);
		}
	}

	public static <T extends Object> T toObject(String json, TypeReference<T> typeRef) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		try {
			return MAPPER.readValue(json, typeRef);
		} catch (Exception e) {
			throw new IllegalArgumentException("Convert json string to object error from value:" + json, e);
		}
	}

	public static <T extends Object> T toObject(String json, Class<T> clazz) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		try {
			return MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			throw new IllegalArgumentException("Convert json string to object error from value:" + json, e);
		}
	}

	public static <T extends Object> T convert(Object obj, Class<T> clazz) {
		try {
			return MAPPER.convertValue(obj, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Convert json string to object error from value:" + obj, e);
		}
	}

	public static <T extends Object> T toObject(byte[] json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			throw new IllegalArgumentException("Convert json byte[] to object error from value:" + new String(json), e);
		}
	}

	public static <T extends Object> T toObject(InputStream stream, Class<T> clazz) {
		if (stream == null) {
			return null;
		}
		try {
			return MAPPER.readValue(stream, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				String content = IOUtils.toString(stream, "utf-8");
				throw new IllegalArgumentException("Convert json stream to object error from: " + content, e);
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
	}

	public static <T extends Object> String toJsonArray(List<T> list) {
		if (list == null) {
			return null;
		}
		try {
			return MAPPER.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Convert json array to string error from list:" + list, e);
		}
	}

	public static <T extends Object> List<T> toArray(String json, Class<T> cls) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		try {
			CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, cls);
			return MAPPER.readValue(json, type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Convert json string to array error from json:" + json, e);
		}
	}

	public static <T extends Object> List<T> toArray(byte[] json, Class<T> cls) {
		if (json == null) {
			return null;
		}
		try {
			CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, cls);
			return MAPPER.readValue(json, type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Convert json bytes to array error from json:" + new String(json), e);
		}
	}

	public static <T extends Object> List<T> toArray(JsonNode json, Class<T> cls) {
		if (json == null) {
			return null;
		}
		try {
			CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, cls);
			return MAPPER.convertValue(json, type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Convert json string to array error from json:" + json, e);
		}
	}

	public static <T extends Object> JsonNode toJsonNode(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof JsonNode) {
			return (JsonNode) value;
		}
		return MAPPER.valueToTree(value);
	}

	public static <T extends Object> JsonNode toJsonNode(String value) {
		if (value == null) {
			return null;
		}
		try {
			return MAPPER.readTree(value);
		} catch (IOException e) {
			throw new IllegalArgumentException("Convert JsonNote to Object error from value:" + value, e);
		}
	}

	public static <T extends Object> JsonNode toJsonNode(byte[] value) {
		if (value == null) {
			return null;
		}
		try {
			return MAPPER.readTree(value);
		} catch (IOException e) {
			throw new IllegalArgumentException("Convert JsonNote to Object error from value:" + value, e);
		}
	}

	public static <T extends Object> T nodeToObject(JsonNode value, Class<T> clazz) {
		if (value == null) {
			return null;
		}
		try {
			return MAPPER.treeToValue(value, clazz);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Convert JsonNote to Object error from value:" + value, e);
		}
	}

	public static String getFieldValue(JsonNode jsonNode, String fieldName) {
		return jsonNode.hasNonNull(fieldName) ? jsonNode.get(fieldName).asText() : null;
	}

	/**
	 * 先从对象变成JsonNode，在通过函数处理，然后转成原来的对象类型
	 *
	 * @param source 原始对象
	 * @param clazz  对象类型
	 * @param func   加工方法
	 * @return 更改后的对象
	 */
	public static <T> T pipe(T source, Class<T> clazz, Function<JsonNode, JsonNode> func) {
		return nodeToObject(func.apply(toJsonNode(source)), clazz);
	}

}
