package com.test.search.service.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.test.search.common.User;
import com.test.search.common.criteria.IndexCriteria;
import com.test.search.common.help.Pagination;
import com.test.search.controller.IndexController;
import com.test.search.service.IndexService;
import com.test.search.util.EsUtils;

/**
 * @author LeoHe
 * @date 2017年12月18日 上午11:04:33
 */
@Service
public class IndexServiceImpl implements IndexService {
	
	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * 索引
	 * @author LeoHe
	 * @date 2017年12月18日 上午11:09:10
	 * @see com.test.search.service.IndexService#index()
	 */
	@Override
	public boolean index(String index, String type) {
		if (StringUtils.hasText(index) && StringUtils.hasText(type)) {
			// 计算count数、总页数
			Long count = jdbc.queryForObject("select count(1) from `user`", Long.class);
			int totalPages = new Pagination("", count, 1, 50).getTotalPages();
			
			String sql = "select * from `user` limit ?, ?";
			RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
			TransportClient client = EsUtils.getClient();
			
			if (client != null) {
				IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type);
				for (long i = 1; i <= totalPages; i++) {
					IndexCriteria criteria = new IndexCriteria();
					criteria.setLimitInfo(i, 50L);
					List<User> users = jdbc.query(sql, new Object[]{criteria.getRowNum(),criteria.getLimitSize()}, rowMapper);
					if (users != null && users.size() > 0) {
						users.forEach(user->{
							XContentBuilder doc = null;
							try {
								doc = jsonBuilder()
										.startObject()
										.field("id", user.getId())
										.field("createTime", user.getCreateTime() == null ? null:(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(user.getCreateTime()))
										.field("name", user.getName())
										.endObject();
								indexRequestBuilder.setId(user.getId().toString()).setSource(doc).get();
								System.out.println(doc.string());
							} catch (IOException e) {
								e.printStackTrace();
								EsUtils.closeClient(client);
							}
						});
					}
					// 每次建索引都记录当前批次数字
					IndexController.successNum = i;
				}
				EsUtils.closeClient(client);
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询索引批次数
	 * @author LeoHe
	 * @date 2017年12月22日 上午11:17:01
	 * @see com.test.search.service.IndexService#queryIndexBatch()
	 */
	@Override
	public Integer queryIndexBatch() {
		Long count = jdbc.queryForObject("select count(1) from `user`", Long.class);
		int totalPages = new Pagination("", count, 1, 50).getTotalPages();
		return totalPages;
	}
}
