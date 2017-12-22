package com.test.search.common.criteria;

import java.util.List;

public class IndexCriteria extends LimitedCriteria {
	private String index; //索引库
	private List<String> indices; //多个索引库
	private String type;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getIndices() {
		return indices;
	}

	public void setIndices(List<String> indices) {
		this.indices = indices;
	}
}
