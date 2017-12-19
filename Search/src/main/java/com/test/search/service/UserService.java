package com.test.search.service;

import java.util.List;

import com.test.search.common.User;
import com.test.search.common.help.Result;

/**
 * 用户
 * @author LeoHe
 * @date 2017年12月19日 下午1:35:56
 */
public interface UserService {

	/**
	 * 查询所有用户
	 * @author LeoHe
	 * @date 2017年12月19日 下午1:38:01
	 */
	List<User> queryAllUsers();

	/**
	 * 添加用户
	 * @author LeoHe
	 * @date 2017年12月19日 下午1:44:24
	 */
	Result addUser(User user);

}
