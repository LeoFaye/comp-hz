package com.test.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.search.common.User;
import com.test.search.common.help.Result;
import com.test.search.common.help.ResultCode;
import com.test.search.service.UserService;

/**
 * 用户
 * @author LeoHe
 * @date 2017年12月19日 下午1:33:06
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;
	
	/**
	 * 查询所有用户
	 * @author LeoHe
	 * @date 2017年12月19日 下午1:40:24
	 */
	@GetMapping("/list")
	public String queryAllUsers(Model model) {
		List<User> users = userServiceImpl.queryAllUsers();
		model.addAttribute("users", users);
		return "/business/user/user-list";
	}
	
	/**
	 * 添加用户
	 * @author LeoHe
	 * @date 2017年12月19日 下午1:42:31
	 */
	@PostMapping("/add")
	public String addUser(User user, Model model, RedirectAttributes attr) {
		Result result = userServiceImpl.addUser(user);
		if (result.getCode().getValue() == ResultCode.SUCCESS.getValue()) {
			attr.addFlashAttribute("result", result);
			return "redirect:/user/list";
		}
		model.addAttribute("result", result);
		return "user-list";
	}
}
