package net.pingfang.common.scripting.nashorn;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.scripting.ScriptExecutionContext;
import net.pingfang.common.scripting.ScriptRunner;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-23 14:17
 */
@NoArgsConstructor
@Slf4j
public class NashornScriptRunner implements ScriptRunner {
	static final ScriptEngineManager manager = new ScriptEngineManager();
	public static final ScriptEngine engine;
	static final ObjectMapper objMapper;
	static final String BIGNUMBER = "/js/bignumber.min.js";
	static final String UTILS = "/js/utils.js";
	static final String BIGNUMBER_JS;
	static final String UTILS_JS;
	static {
		try {
			BIGNUMBER_JS = IOUtils.toString(NashornScriptRunner.class.getResourceAsStream(BIGNUMBER),
					StandardCharsets.UTF_8);
			UTILS_JS = IOUtils.toString(NashornScriptRunner.class.getResourceAsStream(UTILS), StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new RuntimeException("Read JS Resource Error", e);
		}
	}

	public Object run(ScriptExecutionContext scriptContext) {
		List<String> beforeScripts = scriptContext != null && scriptContext.getBeforeScripts() != null
				? scriptContext.getBeforeScripts()
				: Lists.newArrayList();
		beforeScripts.add(BIGNUMBER_JS);
		beforeScripts.add(UTILS_JS);
		long envStart = System.currentTimeMillis();
		ScriptContext newContext = new SimpleScriptContext();
		newContext.setBindings(engine.createBindings(), 100);
		Bindings _binding = newContext.getBindings(100);
		if (!beforeScripts.isEmpty()) {
			beforeScripts.forEach((script) -> {
				try {
					engine.eval(script, _binding);
				} catch (Exception var3) {
					log.error("Executing before script unknow error!", var3);
					throw new RuntimeException(var3);
				}
			});
		}
		if (scriptContext.getCtx() != null) {
			_binding.putAll(scriptContext.getCtx());
		}
		log.debug("prepare env time: " + (System.currentTimeMillis() - envStart));
		try {
			return jvmValue(engine.eval(scriptContext.getFun(), _binding));
		} catch (ScriptException var8) {
			throw new RuntimeException("Script Execution Exception", var8);
		}
	}

	private static Object jvmValue(Object v) {
		if (v instanceof ScriptObjectMirror) {
			ScriptObjectMirror re = (ScriptObjectMirror) v;
			if (re.isArray()) {
				return re.values().stream().map(NashornScriptRunner::jvmValue).collect(Collectors.toList());
			}
			if (re.isExtensible()) {
				return objMapper.convertValue(re, JsonNode.class);
			}
		}
		return v;
	}

	static {
		engine = manager.getEngineByName("nashorn");
		objMapper = new ObjectMapper();
	}
}
