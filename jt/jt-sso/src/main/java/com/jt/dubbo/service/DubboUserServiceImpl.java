package com.jt.dubbo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;


@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * -注册校验
	 */
	@Override
	public void saveUser(User user) {
		// 防止email为null报错,使用电话号码代替

		// 需要将密码进行加密处理
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass).setEmail(user.getPhone()).setCreated(new Date()).setUpdated(user.getCreated());

		userMapper.insert(user);
	}

	/**
	 * -登陆校验 -1.根据用户和密码查询数据库 -结果:没有记录说明用户名或密码不正确return null;
	 * -2.生成加密后的密钥ticket,userJSON串,将数据保存到 -3.返回ticket
	 * 
	 */
	@Override
	public String doLogin(User user,String ip) {
		
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		if (userDB == null) {
			//说明用户名和密码不正确
			return null;
		}
		//表示用户信息正确,保存ticket/ip/userJson
		String uuid =UUID.randomUUID().toString();
		String ticket = DigestUtils.md5DigestAsHex(uuid.getBytes());
		userDB.setPassword("你猜猜!!!!");
		String userJSON = ObjectMapperUtil.toJSON(userDB);
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("JT_TICKET", ticket);
		hash.put("JT_USERJSON", userJSON);
		hash.put("JT_IP", ip);
		jedisCluster.hmset(user.getUsername(), hash);
		jedisCluster.expire(user.getUsername(), 7*24*3600);
		return ticket;
	}
//		User userDB = findUserByUP(user);
//		if (userDB!=null) {
//			//1.生成密钥
//			String uuid = UUID.randomUUID().toString();
//			String ticket = DigestUtils.md5DigestAsHex(uuid.getBytes());
//			//2.将某些敏感的数据进行脱敏处理
//			userDB.setPassword("********");
//			//3.将user对象转化成json串
//			String userJson = ObjectMapperUtil.toJSON(userDB);
//			
//			jedisCluster.setex(ticket, 7*24*3600, userJson);
//			return ticket;
//		}
//		return null;
//	}
	public User findUserByUP(User user) {
		// 将密码进行加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);

		QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
		User selectOne = userMapper.selectOne(queryWrapper);
		return selectOne;
	}
	
	


}
