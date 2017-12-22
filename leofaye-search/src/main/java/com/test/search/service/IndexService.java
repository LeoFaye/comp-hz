package com.test.search.service;

/**
 * @author LeoHe
 * @date 2017年12月18日 上午11:03:04
 */
public interface IndexService {
	/**
	 * 索引
	 * @author LeoHe
	 * @date 2017年12月18日 上午11:06:12
	 */
	boolean index(String index, String type);

	/**
	 * 查询索引批次
	 * @author LeoHe
	 * @date 2017年12月22日 上午11:15:50
	 */
	Integer queryIndexBatch();
}
