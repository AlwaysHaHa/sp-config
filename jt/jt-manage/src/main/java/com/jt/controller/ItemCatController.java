package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	/**根据商品分类ID查询商品信息,并获取商品分类名称 */
	@RequestMapping("/queryItemName")
	public String queryItemName(Long itemCatId) {
		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
	}
	/**
	 * 1.url:/item/cat/list
	 * 2.返回值结果LIst<EasyUITree>
	 * 
	 * SpringMVC动态接收数据
	 * 参数名称:id
	 * 目的:id当做parentId使用
	 * 要求:初始化时id=0
	 * 
	 * @RequestParam说明:
	 * 参数介绍:
	 * name/value:接收用户提交参数
	 * defaultValue:设定默认值
	 * required:该参数是否必传 true
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findItemCatByParentID(@RequestParam(value="id",defaultValue = "0",required =true ) Long parentId){
		
		return itemCatService.findEasyUITree(parentId);
		//return itemCatService.findEasyUITreeByCache(parentId);
	}
	
	
}
