package net.pingfang.common.scripting.nodejs;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-23 14:24
 */
@Data
@Builder
public class JSExecutableDTO {
	final Map<String, Object> ctx;
	final List<String> beforeScripts;
	final String fun;
}
