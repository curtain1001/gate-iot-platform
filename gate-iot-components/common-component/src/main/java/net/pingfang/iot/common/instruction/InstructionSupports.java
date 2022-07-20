package net.pingfang.iot.common.instruction;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.summary.Product;

import com.google.common.collect.Maps;

import jdk.internal.org.objectweb.asm.commons.InstructionAdapter;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 15:56
 */
public class InstructionSupports {
	final static HashMap<Product, InstructionAdapter> supports = Maps.newHashMap();
}
