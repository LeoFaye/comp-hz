package com.test.search.controller;

import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.search.common.criteria.IndexCriteria;
import com.test.search.common.help.Result;
import com.test.search.service.IndexService;
import com.test.search.util.EsUtils;

/**
 * 
 * @author LeoHe
 * @date 2017年12月15日 下午5:32:59
 */
@Controller
public class IndexController {
	
	@Autowired
	IndexService indexServiceImpl;
	
	/**
	 * 首页
	 * @author LeoHe
	 * @date 2017年12月18日 下午4:38:06
	 */
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	/**
	 * 查看Mapping
	 * @author LeoHe
	 * @date 2017年12月18日 下午5:17:36
	 */
	@GetMapping("/view/mapping/{index}")
	public String viewMapping(@PathVariable String index, Model model) {
		TransportClient client = EsUtils.getClient();
		GetFieldMappingsRequestBuilder prepareGetFieldMappings = client.admin().indices().prepareGetFieldMappings(index);
		GetFieldMappingsResponse res = prepareGetFieldMappings.get();
		model.addAttribute("res", res.mappings().toString());
		EsUtils.closeClient(client);
		return "index";
	}
	
	/**
	 * 删除索引库
	 * @author LeoHe
	 * @date 2017年12月18日 下午3:10:52
	 */
	@PostMapping("/index/delete")
	public String deleteIndex(IndexCriteria c, Model model, RedirectAttributes att) {
		boolean result = EsUtils.deleteIndex(c.getIndex());
		Result.handleResult(result, model, att);
		return "index";
	}
	
	/**
	 * 创建索引库
	 * @author LeoHe
	 * @date 2017年12月18日 下午3:42:09
	 */
	@PostMapping("/index/create")
	public String createIndex(IndexCriteria c, Model model, RedirectAttributes att) {
		boolean result = EsUtils.createIndex(c.getIndex());
		Result.handleResult(result, model, att);
		return "index";
	}
	
	/**
	 * 创建Mapping
	 * @author LeoHe
	 * @date 2017年12月18日 下午4:10:18
	 */
	@PostMapping("/mapping/create")
	public String createMapping(IndexCriteria c, Model model, RedirectAttributes att) {
		boolean result = EsUtils.createMapping(c.getIndex(), c.getType());
		Result.handleResult(result, model, att);
		return "index";
	}
	
	/**
	 * 索引
	 * @author LeoHe
	 * @date 2017年12月18日 下午4:25:56
	 */
	@PostMapping("/data/index")
	public String index(IndexCriteria c, Model model, RedirectAttributes att) {
		boolean result = indexServiceImpl.index(c.getIndex(), c.getType());
		Result.handleResult(result, model, att);
		return "index";
	}
}
