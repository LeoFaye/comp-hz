package com.test.search.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.test.search.system.interceptor.ParamInterceptor;

/**
 * 配置类
 * @author LeoHe
 * @date 2017年12月22日 上午10:21:12
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	public ParamInterceptor paramInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(paramInterceptor).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	
}
