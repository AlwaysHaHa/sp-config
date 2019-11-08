package com.jt.util;

import com.jt.pojo.User;

public class ThreadLocalUtil {
	
	private static ThreadLocal<User> thread = new ThreadLocal<>();
	
	//存数据
	public static void set(User user) {
		
		thread.set(user);
	}
	//获取对象
	public static User get() {
		
		return thread.get();
	}
	//删除数据  内存泄漏
	public static void remove() {
		
		thread.remove();
	}
}
