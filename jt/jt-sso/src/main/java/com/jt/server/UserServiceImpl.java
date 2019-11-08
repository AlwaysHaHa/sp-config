package com.jt.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * -校验用户信息
	 * -type : 如果type=1 表示username,如果type=2 表示phone,如果type=3 表示email
	 */
	@Override
	public boolean checkUser(String param, String type) {
        Map<Integer, String> map = new HashMap<>();		
        map.put(1, "username");
        map.put(2, "phone");
        map.put(3, "email");
        String cloumn = map.get(type);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(cloumn, param);
        User user = userMapper.selectOne(queryWrapper);
		return user==null?false:true;
	}


}
