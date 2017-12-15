package com.test.search;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.search.common.IndexCriteria;
import com.test.search.common.User;
import com.test.search.component.ElasticsearchComponent;
import com.test.search.service.SearchService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Hello world!
 *
 */
@Controller
@EnableAutoConfiguration
@ComponentScan 
@RequestMapping(value = "/index")
public class AppController {
	
	@Autowired
	SearchService SearchServiceImpl;
	@Autowired
	Configuration configuration;
	@Autowired
	ElasticsearchComponent elasticsearchComponent;
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(IndexCriteria c, Model model) throws Exception {
		
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("name", c.getInput()));
		
		HighlightBuilder hiBuilder = new HighlightBuilder();
		hiBuilder.preTags("<span style='color:red'>").postTags("</span>").field("name");
		
		TransportClient client = getClient();
		SearchResponse response = client.prepareSearch("zxc")
		        .setTypes("user")
		        .setQuery(boolQuery)
		        .highlighter(hiBuilder)
		        //.setExplain(true)
		        .get();
		
		List<User> manageUsers = new ArrayList<>();
		Gson gson = new GsonBuilder().create();
		
		response.getHits().forEach(h->{
			Map<String, Object> source = h.getSource();
			System.out.println(gson.toJson(source));
			User manageUser = gson.fromJson(gson.toJson(source), User.class);
			Map<String, HighlightField> highlightFields = h.getHighlightFields();
			HighlightField highlightField = highlightFields.get("name");
			Text[] fragments = highlightField.fragments();
			manageUser.setName(fragments[0].string());
			manageUsers.add(manageUser);
			});
		model.addAttribute("manageUsers", manageUsers);
		return "index";
	}
	
	/**
	 * 删除索引库
	 * twitter
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteIndex(IndexCriteria c) throws Exception {
		TransportClient client = getClient();
		DeleteIndexResponse deleteRes = client.admin().indices().prepareDelete(c.getInput()).execute().actionGet();
		client.close();
		if (deleteRes.isAcknowledged())
			return "delete success!";
		return "delete fail";
	}
	
	
	/**
	 * 创建索引库
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public String createIndices(IndexCriteria c) throws IOException {
		SearchServiceImpl.createIndices(c.getInput());
		return "ok!";
	}
	
	/**
	 * 创建mapping
	 */
	@RequestMapping(value = "/create/mapping", method = RequestMethod.POST)
	@ResponseBody
	public String createMapping(IndexCriteria c) {
		XContentBuilder mappingJson = SearchServiceImpl.getMappingJson();
		SearchServiceImpl.createBangMapping(c.getIndex(), c.getType(), mappingJson);
		return "ok";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public String addDoc() {
		SearchServiceImpl.addDoc();
		return "ok";
	}
		
	@RequestMapping(method=RequestMethod.GET)
    public String index(Model model) throws IOException, TemplateException {
		/*Template template = configuration.getTemplate("index.ftl");
		FreeMarkerTemplateUtils.processTemplateIntoString(template, model);*/
		/*configuration.setClassForTemplateLoading(FreeMarkerAutoConfiguration.class, "/templates");
		FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("index.ftl"), model);*/
		model.addAttribute("manageUsers", null);
		return "index";
    }
	
	//获取连接客户端
	private static TransportClient getClient() throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}
	
	//添加索引
	/*@RequestMapping(value = "/index/{index}/{type}/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String indexOne(@PathVariable String index, @PathVariable String type, @PathVariable(required=false)String id) throws IOException {
		
		TransportClient client = getClient();
		XContentBuilder doc = generateJsonDoc(new UserDoc("bob", "what's the matter?"));
		IndexResponse response = client.prepareIndex(index, type, id).setSource(doc).get();
		
		String _index = response.getIndex(); // Index name
		String _type = response.getType(); // Type name
		String _id = response.getId(); // Document ID (generated or not)
		long _version = response.getVersion(); // Version (if it's the first time you index this document, you will get: 1)
		RestStatus status = response.status(); // status has stored current instance statement.
		
		String sb = "_index:"+_index+"    _type:"+_type+"    _id:"+_id+"    _version:"+_version+"    status:"+status;
		System.out.println("response messages:   "+sb);
		client.close();
		return sb;
	}*/
	
	//查询索引
	@RequestMapping(value = "/get/{index}/{type}/{id}", method = RequestMethod.GET)
	public GetResponse getOne(@PathVariable String index, @PathVariable String type, @PathVariable String id) throws UnknownHostException {
		TransportClient client = getClient();
		GetResponse response = client.prepareGet(index, type, id).get();
		System.out.println("get json doc:");
		client.close();
		return response;
	}
	
	//删除索引
	@RequestMapping(value = "/delete/{index}/{type}/{id}")
	public DeleteResponse deleteOne(@PathVariable String index, @PathVariable String type, @PathVariable String id) throws UnknownHostException {
		TransportClient client = getClient();
		DeleteResponse response = client.prepareDelete(index, type, id).get();
		client.close();
		return response;
	}
		
	/*public XContentBuilder generateJsonDoc(UserDoc userDoc) throws IOException {
		XContentBuilder builder = jsonBuilder().startObject().field("user", userDoc.getName())
				.field("postDate", new Date())
				.field("message", userDoc.getMessage()).endObject();
		System.out.println(builder.string());
		return builder;
	}*/
	
	public String indexJsonDoc(TransportClient client, XContentBuilder jsonDoc) {
		IndexResponse response = client.prepareIndex("twitter", "tweet", "1").setSource(jsonDoc).get();
		// Index name
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// status has stored current instance statement.
		RestStatus status = response.status();
		String sb = "_index:"+_index+"    _type:"+_type+"    _id:"+_id+"    _version:"+_version+"    status:"+status;
		System.out.println("response messages:   "+sb);
		return sb;
		
	}
	
	public GetResponse getJsonDoc(TransportClient client, String index, String type, String id) {
		GetResponse response = client.prepareGet(index, type, id).get();
		System.out.println("get json doc:");
		return response;
	}

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppController.class, args);
    }
}
