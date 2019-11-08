package com.jt.service;

import com.jt.desc.ItemDesc;
import com.jt.pojo.Item;


public interface ItemService {

	Item findItemById(Long itemId);

	ItemDesc findItemDescById(Long itemId);

}
