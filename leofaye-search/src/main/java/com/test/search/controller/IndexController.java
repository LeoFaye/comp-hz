package com.test.search.controller;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
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
import com.test.search.common.help.ResultCode;
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
	
	public static Long successNum = null;
		
	/**
	 * 创建索引首页
	 * @author LeoHe
	 * @date 2017年12月18日 下午4:38:06
	 */
	@GetMapping("/index/create")
	public String index(Model model, HttpServletRequest request) {
		
		// 展示所有索引方格
		Integer batchNum = indexServiceImpl.queryIndexBatch();
		model.addAttribute("batchNum", batchNum);
		
		
		// 查询已存在的索引库
		TransportClient client = EsUtils.getClient();
		GetIndexResponse res = client.admin().indices().prepareGetIndex().get();
		String[] indices = res.getIndices();
		model.addAttribute("indices", indices);
		
		
		// 成功的绿色方格
		model.addAttribute("successNum", successNum);
		return "index-create";
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
	 * 查看已有的索引库
	 * @author LeoHe
	 * @date 2017年12月18日 下午5:53:45
	 */
	@GetMapping("/view/indices")
	public String viewIndices(Model model) {
		TransportClient client = EsUtils.getClient();
		GetIndexResponse res = client.admin().indices().prepareGetIndex().get();
		String[] indices = res.getIndices();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indices.length; i++) {
			sb.append(indices[i]).append(", ");
		}
		if (sb.length() > 0) {
			model.addAttribute("index", sb.substring(0, sb.length()-1));
		}
		return "index";
	}
	
	/**
	 * 删除索引库
	 * @author LeoHe
	 * @date 2017年12月18日 下午3:10:52
	 */
	@PostMapping("/index/delete")
	public String deleteIndex(IndexCriteria c, Model model, RedirectAttributes att) {
		if(c.getIndices() != null && !c.getIndices().isEmpty()) {
			c.getIndices().forEach(i->EsUtils.deleteIndex(i));
			att.addFlashAttribute("result", new Result(ResultCode.SUCCESS, "操作成功"));
			return "redirect:/index/create";
		}
		return "index-create";
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
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = indexServiceImpl.index(c.getIndex(), c.getType());
				Result.handleResult(result, model, att);
			}
		});
		thread.start();
		return "redirect:/index/create";
	}
}
