package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(check = false)
	private DubboCartService cartService;

	@RequestMapping("/show")
	public String show(Model model,HttpServletRequest request) {
		//User user = (User)request.getAttribute("JT_USER");
		User user = ThreadLocalUtil.get();
		Long userId =user.getId();
		List<Cart> list = new ArrayList<Cart>();
		list = cartService.findCartList(userId);
		model.addAttribute("cartList",list);
		return "cart";
	}
	
	@ResponseBody
	@RequestMapping("/update/num/{itemId}/{num}")
	public SysResult updateCartNum(Cart cart) {
		User user = ThreadLocalUtil.get();
		Long userId =user.getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
	}
	
	//http://www.jt.com/cart/delete/562379.html
	@RequestMapping("/delete/{itemId}")
	public String deleteByItemId(Cart cart) {
		User user = ThreadLocalUtil.get();
		Long userId =user.getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}

	//http://www.jt.com/cart/add/562379.html
	@RequestMapping("/add/{itemId}")
	public String addByItemId(Cart cart) {
		User user = ThreadLocalUtil.get();
		Long userId =user.getId();
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}

}















