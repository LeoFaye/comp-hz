package com.test.search.common.criteria;

public class LimitedCriteria {
	private Long rowNum;
	private Long limitSize;
	
	public void setLimitInfo(Long pageNum, Long pageSize) {
		this.rowNum = (pageNum-1) * pageSize;
		this.limitSize = pageSize;
	}

	public Long getRowNum() {
		return rowNum;
	}

	public Long getLimitSize() {
		return limitSize;
	}
	
	
}
