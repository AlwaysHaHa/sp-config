package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component //将对象交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	private static final String JTUSER = "JT_USER";
	/*
	 * 方法执行之前
	 * Boolean
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//System.out.println("方法执行之前:");
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		
		if (!StringUtils.isEmpty(ticket)) {
			
			String userJSON = jedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJSON)) {
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//request.setAttribute(JTUSER, user);
				ThreadLocalUtil.set(user);
				System.out.println("用户信息保存到域中");
				return true; //表示程序放行
			}
					
		}
		//一般拦截器中的false和重定向联用
		//应该重定向到登录页面
		response.sendRedirect("/user/login.html");
		return false; //表示拦截
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("方法执行之后:post");
	}
	/**
	 * 方法完成的最后阶段
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		request.removeAttribute(JTUSER);
		System.out.println("用户数据删除");
	}

}
