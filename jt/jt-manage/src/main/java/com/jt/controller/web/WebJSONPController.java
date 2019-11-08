package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.desc.ItemDesc;

@RestController

public class WebJSONPController {
	
	/**
	 * 要求返回值结果callback(json)
	 * 
	 */
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1001L).setItemDesc("跨域访问!!");
		return new JSONPObject(callback, itemDesc);
	} 
	
//	@RequestMapping("/web/testJSONP")
//	public String jsonp(String callback) {
//		ItemDesc itemDesc = new ItemDesc();
//		itemDesc.setItemId(1001L).setItemDesc("跨域访问!!");
//		String json = ObjectMapperUtil.toJSON(itemDesc) ;
//		return callback+"("+json+")";
//	} 
}
