package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.Cache_Find;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;


@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private ItemCatMapper itemCatMapper;

	@Override
	public ItemCat findItemCatById(Long itemCatId) {

		return itemCatMapper.selectById(itemCatId);
	}
	/**
	 * vo对象~~转换~~POJO
	 * 思路:
	 *  1.根据parentId查询一级商品分类信息List<ItemCat>
	 *  2.遍历List<ItemCat>~~~itemCat(DB)
	 *  封装
	 */
	@Override
	@Cache_Find
	public List<EasyUITree> findEasyUITree(Long parentId) {

		List<ItemCat> itemCatList = findItemCatByParentId(parentId);
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			//如果是父级closed 否则open
			String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree easyUITree = new EasyUITree(id,text,state);
			treeList.add(easyUITree);
		}
		return treeList;
	}

	public List<ItemCat> findItemCatByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper=new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		return itemCatList;
	}

	/*
	@Autowired
	private Jedis jedis;
	//ObjectMapperUtil omu = new ObjectMapperUtil();
	@Override
	public List<EasyUITree> findEasyUITreeByCache(Long parentId) {

		//		String id = String.valueOf(parentId);
		//查询缓存
		//		String data = jedis.get(id);
		//		if(data==null) {
		//			
		//			List<EasyUITree> fTree = findEasyUITree(parentId);
		//			jedis.set(id, omu.toJSON(fTree));
		//			return fTree;
		//		}else {
		//			List<EasyUITree> ui=omu.toObject(data, ArrayList.class);
		//			return ui;
		//		}

		String key = "ITEM_CAT_"+parentId;
		//查询缓存
		String result = jedis.get(key);
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		if(StringUtils.isEmpty(result)) {
			//缓存没数据查询数据库
			treeList = findEasyUITree(parentId);
			System.out.println("查询数据库");
			String value = ObjectMapperUtil.toJSON(treeList);
			jedis.set(key, value);

		}else {
			//缓存有数据直接返回
			
			treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
		}
		return treeList;

	}	*/
}











