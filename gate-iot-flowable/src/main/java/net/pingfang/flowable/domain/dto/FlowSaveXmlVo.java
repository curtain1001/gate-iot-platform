package net.pingfang.flowable.domain.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author XuanXuan
 * @date 2021/3/28 19:48
 */
@Data
public class FlowSaveXmlVo implements Serializable {

	private static final long serialVersionUID = 4607870959388764049L;
	/**
	 * 流程名称
	 */
	private String name;

	/**
	 * 流程分类
	 */
	private String category;

	/**
	 * xml 文件
	 */
	private String xml;
}
