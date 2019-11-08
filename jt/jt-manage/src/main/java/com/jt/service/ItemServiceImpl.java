package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.desc.ItemDesc;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//1.获取商品的记录总数
		int total = itemMapper.selectCount(null);
		//2.获取分页后的数据
		int start = (page - 1) * rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		/*
		 * EasyUITable table = new EasyUITable();
		 * table.setTotal(total);
		 * table.setRows(itemList);
		 */
		//构造方法赋值
		return new EasyUITable(total,itemList);
	}

	@Override
	@Transactional //控制事务
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		itemMapper.insert(item);
		//当数据入库之后才有主键
		//mybatis特性:数据库操作中主键自增之后会自动回填主键信息
		
		//新增商品详情
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	@Transactional
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(item.getCreated());
		itemMapper.updateById(item);
		itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
		}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		//itemMapper.deleteBatchIds(idList);
		itemMapper.deleteItems(ids);
		itemDescMapper.deleteBatchIds(idList);
	}

	@Override
	public void updateItemsState(Integer status, Long[] ids) {
		for(Long id:ids) {
			Item item = new Item();
			item.setId(id).setStatus(status).setUpdated(new Date());
			itemMapper.updateById(item);
		}
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long id) {
		
		return itemMapper.selectById(id);
	}
	
	
	
	
	
	
	
	
}
