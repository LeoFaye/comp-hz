package com.test.search.service;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface SearchService {

	/**
	 * 创建索引库
	 */
	void createIndices(String indices);
	
	/**
	 * 获取mappingJson
	 */
	XContentBuilder getMappingJson();

	/**
	 * 创建mapping
	 */
	void createBangMapping(String indices, String type, XContentBuilder mappingXContentBuilder);

	/**
	 * 添加索引
	 */
	String addDoc();
	
}
