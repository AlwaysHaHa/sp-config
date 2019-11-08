package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.desc.ItemDesc;
import com.jt.pojo.Item;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		return itemService.findItemByPage(page,rows); 
	}
	//新增
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		
//		try {
//			itemService.saveItem(item);
//			return SysResult.success();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return SysResult.fail();
//		}
		
		itemService.saveItem(item, itemDesc);
		return SysResult.success();
	}
	//修改
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
			itemService.updateItem(item,itemDesc);
			return SysResult.success();
	}
	//删除
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	//下架
	@RequestMapping("/instock")
	public SysResult instock(Integer status,Long[] ids) {
		status=2;//下架.
		itemService.updateItemsState(status,ids);
		return SysResult.success();
	}
	//上架
	@RequestMapping("/reshelf")
	public SysResult reshelf(Integer status,Long[] ids) {
		status=1;//上架
		itemService.updateItemsState(status,ids);
		return SysResult.success();
	}
	
	/**
	 * 实现商品详情查询
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		//将数据回传给页面
		return SysResult.success(itemDesc);
	}
}









