package net.pingfang.common.scripting.nashorn;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Maps;

import net.pingfang.common.scripting.ScriptExecutionContext;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-23 14:38
 */
@RunWith(SpringRunner.class)
public class NashornScriptRunnerTest {

	@Test
	public void runnerTest() throws InterruptedException {
		String script = "print('Hello World'+name)";
		Map<String, Object> map = Maps.newHashMap();
		map.put("name", "wangchao");
		NashornScriptRunner runner = new NashornScriptRunner();
		ScriptExecutionContext context = ScriptExecutionContext.builder() //
				.fun(script)//
				.ctx(map)//
				.build();
		runner.run(context);
	}
}
