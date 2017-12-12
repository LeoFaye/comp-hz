package com.test.search.doc;

public class IndexCriteria extends LimitedCriteria {
	
	private String input;
	
	private String index; // 索引库
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

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
}
