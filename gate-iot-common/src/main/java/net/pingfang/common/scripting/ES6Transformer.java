package net.pingfang.common.scripting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ES6Transformer {

	final static ScriptEngineManager manager;
	final static ScriptEngine engine;

	static {
		try {
			manager = new ScriptEngineManager();
			engine = manager.getEngineByName("nashorn");
			engine.eval("load('classpath:es6/polyfill.min.js');" //
					+ "load('classpath:es6/babel.min.js');" //
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String transform(String source) {
		try {
			engine.put("source", source);
			String code = (String) engine
					.eval("Babel.transform(source, { presets: ['es2015'],sourceType:'script' }).code;");
			return code;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
