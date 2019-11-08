package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.server.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	/**
	 * -根据用户信息实现数据的校验
	 * -返回值结果
	 *     true:表示数据已经存在
	 *     false:用户可以使用该数据
	 *     Type为类型，可选参数1 username、2 phone、3 email
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 */
	@RequestMapping("check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable String type,String callback) {
		
		boolean data = userService.checkUser(param,type);
		
		return new JSONPObject(callback, SysResult.success(data));
	}
	
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * -用户信息的回显
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(
			@PathVariable String ticket,
			String callback) {
		
		String userJSON = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			//用户使用的ticket有问题
			return new JSONPObject(callback, SysResult.fail());
		}
			
		return new JSONPObject(callback,SysResult.success(userJSON));
	}

	
	
	

}
