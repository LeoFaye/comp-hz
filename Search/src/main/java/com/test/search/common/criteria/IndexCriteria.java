package com.test.search.common.criteria;

public class IndexCriteria extends LimitedCriteria {
	private String index; //索引库
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
}
