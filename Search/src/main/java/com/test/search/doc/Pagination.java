package com.test.search.doc;

import java.io.Serializable;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;


public class Pagination implements Serializable {
	private static final long serialVersionUID = -3563669242430049432L;

	final static Logger logger = (Logger) LoggerFactory.getLogger(Pagination.class);
	
	public static int PAGE_SIZE_DISPLAY = 15;
	public static final int PAGE_SIZE_DISPLAY_FIVE = 5;
	public static final int MAX_PAGE_ITEM_DISPLAY = 15;

	public int currentNumber;
	public Long totalRecordCount;
	
	public int pageSize;
	public int maxPageItems;
	
	public int totalPages;
	public String url;
	public String previousPageUrl;
	public String nextPageUrl;

	/**
	 * 
	 * @param url
	 * @param totalRecordCount
	 * @param currentNumber
	 * @param params
	 *	* param[0] will be pageSize.
		* param[1] will be max page item to be dispayed.
		* 
	 */
	public Pagination(String url,  Long totalRecordCount, Integer currentNumber, Integer...params) {		
		
		if(totalRecordCount == null || currentNumber == null){
			logger.error("Parameter exception:totalRecordCount&currentNumber can not be null!");

		}
		
		this.url = url;
		this.totalRecordCount = totalRecordCount;
		this.currentNumber = currentNumber;
		
		
		/**
		 * param[0] will be pageSize.
		 * param[1] will be max page item to be dispayed.
		 */
		
		if(params.length == 0){
			pageSize = PAGE_SIZE_DISPLAY;
			maxPageItems = MAX_PAGE_ITEM_DISPLAY;
		}else if(params.length == 1){
			pageSize = params[0];
			maxPageItems = MAX_PAGE_ITEM_DISPLAY;
		}else{
			pageSize = params[0];
			maxPageItems = params[1];
		}
		
		double a =  (double)totalRecordCount / (double)pageSize;
		
		totalPages = (int) Math.ceil(a);
		if(totalPages<this.currentNumber){
			this.currentNumber=totalPages;
		}
		if(this.currentNumber<=0){
			this.currentNumber=1;
		}
		int start, size;
		if (totalPages <= maxPageItems) {
			start = 1;
			size = totalPages;
		} else {
			if (currentNumber <= maxPageItems - maxPageItems
					/ 2) {
				start = 1;
				size = maxPageItems;
			} else if (currentNumber >= totalPages - maxPageItems / 2) {
				start = totalPages - maxPageItems + 1;
				size = maxPageItems;
			} else {
				start = currentNumber - maxPageItems / 2;
				size = maxPageItems;
			}
		}
	}
	
	public Pagination() {
		// TODO Auto-generated constructor stub
	}

	public int getTotalPages() {
		return totalPages;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}

	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public boolean isHasPreviousPage() {
		return currentNumber > 1;
	}

	public boolean isHasFirstPage() {
		return !isHasPreviousPage();
	}

	public boolean isHasNextPage() {
		return ((currentNumber) * pageSize) < totalRecordCount;
	}

	public boolean isHasLastPage() {
		return !isHasNextPage();
	}

	public String getPreviousPageUrl() {
		if (this.isHasPreviousPage()) {
			return this.url + "/" + (this.currentNumber - 1);
		} else {
			return "#";
		}
	}
	
	public String getNextPageUrl() {

		if (this.isHasNextPage()) {
			return this.url + "/" + (this.currentNumber + 1);
		} else {
			return "#";
		}

	}
	
	
	public int getPageSize(){
		return this.pageSize;
	}
}
