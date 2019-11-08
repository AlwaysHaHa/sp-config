package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.dubbo.service.DubboCartService;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartMapperImpl implements DubboCartService{
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartList(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);
		return cartMapper.selectList(queryWrapper);
		
	}
	/***
	 * sql:update tb_cart set num = #{num},updated = #{updated} where user_id=#{id} and item_id=#{itemId}
	 */
	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("item_id", cart.getItemId())
		.eq("user_id", cart.getUserId());
		
		cartMapper.update(cartTemp, updateWrapper);
		
	}

	//添加到购物车
	/**
	 * 1.判断购物车中是否有记录
	 * null:新增
	 * !null:num,updated更新操作
	 */
	
	@Override
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if (cartDB == null) {
			//用户第一次新增
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//用户不是第一次新增该商品
			int num = cartDB.getNum() + cart.getNum();
			//cartDB.setNum(num).setUpdated(new Date());
			//更新数据,num,updated
			Cart cartTemp = new Cart();
			cartTemp.setId(cartDB.getId()).setNum(num).setUpdated(new Date());
			cartMapper.updateById(cartTemp);
		}
		
	}
	@Override
	public void deleteCart(Cart cart) {
		//执行删除
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
		
	}
	
}
