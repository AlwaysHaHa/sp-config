package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	/**
	 * restFul风格
	 * 		语法:1.参数必须使用/分割
	 * 			2.参数使用{}包裹
	 * 			3.使用@PathVariable注解修饰
	 * 		规则:如果参数的名称与对象的属性一致,可以直接使用对象接收
	 * 		高级应用:
	 * 			eg1:根据请求方式,实现不同操作
	 * 			请求方式: get 查询操作  post 入库操作     put 更新操作   delete 删除操作
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
}
