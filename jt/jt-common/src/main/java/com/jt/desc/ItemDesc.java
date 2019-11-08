package com.jt.desc;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jt.pojo.BasePojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo{
	//id必须与item的id一致
	@TableId
	private Long itemId;
	private String itemDesc;
}
