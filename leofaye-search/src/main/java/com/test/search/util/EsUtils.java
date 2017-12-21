package com.test.search.util;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.util.StringUtils;


/**
 * es 相关操作
 * @author LeoHe
 * @date 2017年12月15日 下午5:45:29
 */
public class EsUtils {

	/**
	 * 获取client
	 * @author LeoHe
	 * @date 2017年12月15日 下午5:50:04
	 */
	public static TransportClient getClient() {
		TransportClient client = null;
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			closeClient(client);
		}
		return client;
	}
	
	/**
	 * 关闭client
	 * @author LeoHe
	 * @date 2017年12月15日 下午5:51:37
	 */
	public static void closeClient(TransportClient client) {
		if (client != null) client.close();
	}
		
	/**
	 * 创建索引库
	 * @author LeoHe
	 * @date 2017年12月15日 下午5:57:24
	 */
	public static boolean createIndex(String index) {
		if (StringUtils.hasText(index)) {
			CreateIndexRequest request = new CreateIndexRequest(index);
			TransportClient client = getClient();
			if (client != null && request != null) {
				CreateIndexResponse res = client.admin().indices().create(request).actionGet();
				closeClient(client);
				if (res.isAcknowledged()) return true;
			}
		}
		return false;
	}
	
	/**
	 * 删除索引库
	 * @author LeoHe
	 * @date 2017年12月18日 下午3:05:03
	 */
	public static boolean deleteIndex(String index) {
		if(StringUtils.hasText(index)) {
			TransportClient client = getClient();
			if (client != null) {
				DeleteIndexResponse res = client.admin().indices().prepareDelete(index).execute().actionGet();
				closeClient(client);
				if (res.isAcknowledged()) return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取mapping JSON
	 * @author LeoHe
	 * @date 2017年12月18日 上午10:31:58
	 */
	public static XContentBuilder getMappingJSON() {
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
		} catch (IOException e) {
			e.printStackTrace();
			if (mapping != null) mapping.close();
		}
		return mapping;
	}
	
	/**
	 * 创建mapping
	 * @author LeoHe
	 * @date 2017年12月18日 上午10:56:16
	 */
	public static boolean createMapping(String index, String type) {
		if (StringUtils.hasText(index) && StringUtils.hasText(type)) {
			PutMappingRequest mapping = Requests.putMappingRequest(index).type(type).source(getMappingJSON());
		    TransportClient client = getClient();
		    if (client != null && mapping != null) {
		    	PutMappingResponse res = client.admin().indices().putMapping(mapping).actionGet();
		    	closeClient(client);
		    	if (res.isAcknowledged()) return true;
		    }
		}
		return false;
	}
}
