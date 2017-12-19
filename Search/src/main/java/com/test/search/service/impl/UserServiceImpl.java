package com.test.search.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.test.search.common.User;
import com.test.search.common.help.Result;
import com.test.search.service.UserService;

/**
 * 用户
 * @author LeoHe
 * @date 2017年12月19日 下午1:46:05
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JdbcTemplate jdbc;
	
	/**
	 * 查询所有的用户
	 * @author LeoHe
	 * @date 2017年12月19日 下午1:46:05
	 * @see com.test.search.service.UserService#list()
	 */
	@Override
	public List<User> queryAllUsers() {
		RowMapper<User> userMapper = new BeanPropertyRowMapper<>(User.class);
		List<User> users = jdbc.query("select * from `es-test`.`user` order by id desc;", userMapper);
		return users;
	}

	/**
	 * 添加用户
	 * @author LeoHe
	 * @date 2017年12月19日 下午1:46:05
	 * @see com.test.search.service.UserService#addUser(com.test.search.common.User)
	 */
	@Override
	public Result addUser(User user) {
		jdbc.update("INSERT INTO `es-test`.`user` (`name`, `createTime`, `age`) VALUES (?, NOW(), ?);",
				new Object[]{user.getName(), user.getAge()});
		return Result.SUCCESS("操作成功");
	}

}
