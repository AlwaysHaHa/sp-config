package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.dubbo.service.DubboUserService;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Reference(check = false)
	private DubboUserService userService;
	private static final String TICKET ="JT_TICKET";
	@Autowired
	private JedisCluster jedisCluster;
	
	//注册:http://www.jt.com/user/register.html
	//登录:http://www.jt.com/user/login.html
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}
	
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		userService.saveUser(user);
		return SysResult.success();
	}
	
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletRequest request,HttpServletResponse response) {
		//1.获取用户ip信息
		String ip = IPUtil.getIpAddr(request);
		//2.完成用户信息的校验
		String ticket = userService.doLogin(user,ip);
		if(StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		//写入cookie
		CookieUtil.addCookie(request, response, "JT_TICKET", ticket, 7*24*3600, "jt.com");
		CookieUtil.addCookie(request, response, "JT_USERNAME", user.getUsername(), 7*24*3600, "jt.com");
		return SysResult.success();
	}
//		String ticket = userService.doLogin(user);
//		if (StringUtils.isEmpty(ticket)) {
//			//表示返回值不正确,给用户提示信息
//			return SysResult.fail("用户名或密码错误");
//		}
//		//将ticket保存到客户端的cookie中
//		Cookie ticketCookie = new Cookie(TICKET, ticket);
//		ticketCookie.setMaxAge(7*24*3600);  //7天有效
//		//cookie的权限控制
//		ticketCookie.setPath("/");
//		//cookie实现共享
//		ticketCookie.setDomain("jt.com");
//		response.addCookie(ticketCookie);
//		return SysResult.success();
//	}
	
	/**
	 *  前提:获取
	 * 0.首先获取JT_TICKET的Cookie的值ticket
	 * 1.删除cookie 名为:JT_TICKET
	 * 2.删除redis 根据ticket值4
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request ,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		if(cookies.length>0) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("JT_TICKET")) {
				ticket = cookie.getValue();
				break;
			}
		}
		}
		if (!StringUtils.isEmpty(ticket)) {
			jedisCluster.del(ticket);
			Cookie cookie = new Cookie("JT_TICKET", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain("jt.com");
			response.addCookie(cookie);
		}
		return "redirect:/";
	}
	@RequestMapping("/query/{ticket}/{username}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,@PathVariable String username,String callback,HttpServletRequest request) {
		//1.校验用户的ip地址
		String ip = IPUtil.getIpAddr(request);
		String localIP = jedisCluster.hget(username, "JT_IP");
		if (!ip.equalsIgnoreCase(localIP)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		//2.校验ticket信息
		String localTicket = jedisCluster.hget(username, "JT_TICKET");
		if (!ticket.equalsIgnoreCase(localTicket)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		//3.短信验证 200
		//4.人脸识别/指纹 移动端
		//3.说明用户信息正确
		String userJSON = jedisCluster.hget(username, "JT_USERJSON");
		return new JSONPObject(callback,SysResult.success(userJSON));
	}
	
}



















