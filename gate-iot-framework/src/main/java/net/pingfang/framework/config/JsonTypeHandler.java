package net.pingfang.framework.config;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-05 9:42
 */
@MappedTypes(value = JsonNode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR, includeNullJdbcType = true)
public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T> {
	private static final ObjectMapper mapper = new ObjectMapper();
	private Class<T> clazz;

	public JsonTypeHandler(Class<T> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("Type argument cannot be null");
		this.clazz = clazz;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, this.toJson(parameter));
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return this.toObject(rs.getString(columnName), clazz);
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.toObject(rs.getString(columnIndex), clazz);
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return this.toObject(cs.getString(columnIndex), clazz);
	}

	private String toJson(T object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private T toObject(String content, Class<?> clazz) {
		if (content != null && !content.isEmpty()) {
			try {
				return (T) mapper.convertValue(content, clazz);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	static {
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
}
