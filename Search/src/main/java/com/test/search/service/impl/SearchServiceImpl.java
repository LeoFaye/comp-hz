package com.test.search.service.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.test.search.common.IndexCriteria;
import com.test.search.common.Pagination;
import com.test.search.common.User;
import com.test.search.component.ElasticsearchComponent;
import com.test.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	ElasticsearchComponent elasticsearchComponent;
	@Autowired
	JdbcTemplate jdbc;
		
	/**
	 * 查询后台人员数量
	 */
	public Long queryManageUsers() {
		String sql = "select count(1) from `user`";
		Long count = jdbc.queryForObject(sql, Long.class);
		return count;
	}
	
	/**
	 * 创建索引库
	 */
	@Override
	public void createIndices(String indices) {
		TransportClient client = null;
		try {
			CreateIndexRequest request = new CreateIndexRequest(indices);
			client = elasticsearchComponent.getClient();
			client.admin().indices().create(request).actionGet();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (client != null) client.close();
		}
		
	}
	
	/**
	 * 创建mapping
	 * @param indices
	 * @param type
	 * @param mappingXContentBuilder
	 */
	@Override
	public void createBangMapping(String indices, String type, XContentBuilder mappingXContentBuilder){
	    PutMappingRequest mapping = Requests.putMappingRequest(indices).type(type).source(mappingXContentBuilder);
	    TransportClient client = null;
		try {
			client = elasticsearchComponent.getClient();
			client.admin().indices().putMapping(mapping).actionGet();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (client != null) client.close();
		}
	}
	
	/**
	 * 获取mappingJson
	 */
	@Override
	public XContentBuilder getMappingJson() {
		XContentBuilder mapping = null;
		try {
			mapping = jsonBuilder()
				.startObject()
					.startObject("properties")
						.startObject("name")
							.field("type", "string")
							.field("analyzer", "ik_max_word")
							.field("search_analyzer", "ik_smart")
						.endObject()
						.startObject("createTime")
							.field("type", "date")
							.field("format", "yyyy-MM-dd HH:mm:ss.SSS")
						.endObject()
						.startObject("@timestamp")
							.field("type", "date")
							.field("format", "yyyy-MM-dd HH:mm:ss.SSS")
						.endObject()
					.endObject()
				.endObject();
			System.out.println(mapping.string());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping;
	}
	
	
	/**
	 * 添加索引
	 */
	@Override
	public String addDoc() {
		TransportClient client = null;
		try {
			client = elasticsearchComponent.getClient(); //拿到连接
			Long count = queryManageUsers();
			Pagination p = new Pagination("", count, 1, 50);
			int totalPages = p.getTotalPages(); // 总页数
			
			String sql = "select * from `user` limit ?, ?";
			RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
			
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex("zxc", "user"); //拿到索引生成器
			
			for (long i = 1; i <= totalPages; i++) {
				IndexCriteria criteria = new IndexCriteria();
				criteria.setLimitInfo(i, 50L);
				List<User> users = jdbc.query(sql, new Object[]{criteria.getRowNum(),criteria.getLimitSize()}, rowMapper);
				for (User mu : users) {
					Date date = mu.getCreateTime();
					String s=null;
					if (date != null) {
						s = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(mu.getCreateTime());
					}
					XContentBuilder doc = jsonBuilder()
							.startObject()
							.field("id", mu.getId()) // id
							.field("createTime", s)
							.field("name", mu.getName())
							.endObject();
						indexRequestBuilder.setId(mu.getId().toString()).setSource(doc).get();
						System.out.println(doc.string());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) client.close();
		}
		return "ok";
	}

}
