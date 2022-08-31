package net.pingfang.common.scripting.nodejs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.scripting.ScriptExecutionContext;
import net.pingfang.common.scripting.ScriptRunner;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-23 14:23
 */
@Slf4j
public class RemoteNodeJSScriptRunner implements ScriptRunner {
	final Client client;
	final String endpoint;
	final ObjectMapper objMapper = new ObjectMapper();

	public RemoteNodeJSScriptRunner(Client client, String endpoint) {
		this.endpoint = endpoint;
		this.client = client;
	}

	public Object run(ScriptExecutionContext scriptContext) {
		List<String> beforeScripts = scriptContext != null && scriptContext.getBeforeScripts() != null
				? scriptContext.getBeforeScripts()
				: Collections.emptyList();
		Map<String, Object> ctx = scriptContext != null && scriptContext.getCtx() != null ? scriptContext.getCtx()
				: Maps.newHashMap();
		JSExecutableDTO dto = JSExecutableDTO.builder()//
				.beforeScripts(beforeScripts) //
				.ctx(ctx) //
				.fun(scriptContext != null ? scriptContext.getFun() : null) //
				.build();

		try {
			String payload = this.objMapper.writeValueAsString(dto);
			log.trace("Script Context: {}", payload);
			Response res = this.client.target(this.endpoint).path("/eval").request().post(Entity.json(payload));
			String re = (String) res.readEntity(String.class);
			log.debug("Result: {} - '{}'", res.getStatus(), re);
			JsonNode v = this.objMapper.readTree(re);
			return jvmValue(v);
		} catch (Exception var9) {
			log.error("Error Sending JS Eval", var9);
			throw new RuntimeException("Remote Script Execution Error", var9);
		}
	}

	private static Object jvmValue(JsonNode v) {
		if (v == null) {
			return null;
		} else {
			if (v.isValueNode()) {
				if (v.isInt()) {
					return v.intValue();
				}

				if (v.isBigDecimal()) {
					return v.decimalValue();
				}

				if (v.isBoolean()) {
					return v.booleanValue();
				}

				if (v.isDouble()) {
					return v.doubleValue();
				}

				if (v.isFloat()) {
					return v.floatValue();
				}

				if (v.isBigInteger()) {
					return v.bigIntegerValue();
				}

				if (v.isLong()) {
					return v.longValue();
				}

				if (v.isShort()) {
					return v.shortValue();
				}

				if (v.isNumber()) {
					return v.numberValue();
				}

				if (v.isTextual()) {
					return v.textValue();
				}
			} else if (v.isArray()) {
				ArrayNode arr = (ArrayNode) v;
				return Arrays.asList(Iterators.toArray(
						Iterators.transform(arr.iterator(), RemoteNodeJSScriptRunner::jvmValue), Object.class));
			}
			return v;
		}
	}
}
