package com.test.search.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 参数拦截器
 * @author LeoHe
 * @date 2017年12月22日 上午10:17:46
 */
@Component
public class ParamInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = Logger.getLogger(ParamInterceptor.class);
	
	/**
	 * @author LeoHe
	 * @date 2017年12月22日 上午10:18:03
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		StringBuffer url = new StringBuffer()
			.append(request.getScheme()).append("://")
			.append(request.getServerName())
			.append(":")
			.append(request.getServerPort())
			.append(request.getContextPath());
			logger.info("url------------> : " + url);
			request.setAttribute("url", url);
			return super.preHandle(request, response, handler);
	}
}
