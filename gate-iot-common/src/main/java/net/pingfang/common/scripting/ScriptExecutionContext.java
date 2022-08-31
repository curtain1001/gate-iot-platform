package net.pingfang.common.scripting;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-23 14:16
 */
@Data
@AllArgsConstructor
@Builder
public class ScriptExecutionContext {
	final List<String> beforeScripts;
	final String fun;
	final Map<String, Object> ctx;
}
