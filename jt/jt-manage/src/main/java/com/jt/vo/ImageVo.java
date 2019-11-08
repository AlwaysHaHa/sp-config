package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVo {
	private Integer error=0;  //0表示没错..1表示有错
	private String url;			//图片的虚拟路径
	private Integer width;
	private Integer height; 
	
	//指定失败的方法
	public static ImageVo fail() {
		return new ImageVo(1, null, null, null);
	}
}
