package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Reference(check = false)
	private DubboOrderService orderService;
	
	@Reference(check = false)
	private DubboCartService cartService;
	/**
	 * 实现
	 * 
	 * 回显数据说明:${carts}
	 * @return
	 */
	@RequestMapping("/create")
	public String create(Model model) {
		Long userId = ThreadLocalUtil.get().getId();
		List<Cart> carts = cartService.findCartList(userId);
		model.addAttribute("carts",carts);
		return "order-cart";
	}
	/**
	 * 业务说明:
	 * 	完成
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOder(Order order) {
		String orderId = orderService.saveOrder(order);
		
		return SysResult.success(orderId);
	}
	
	/**
	 * 根据
	 * @param id
	 * @return
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {
		Order order = orderService.findOrderById(id);
		model.addAttribute("order",order);
		
		return "success";
	}
	
}
