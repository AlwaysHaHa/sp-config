package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVo;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	
	//定义本地磁盘路径
	@Value("${image.localDirPath}")
	String localDirPath = "D:/1-jt-software/jt-images/";

	//定义虚拟路径地址
	@Value("${image.urlDirPath}")
	private String urlDirPath="http://image.jt.com/";
	/**
	 * 实现的思路:
	 * 1.校验图片类型:jpg/jpeg/png/...
	 * 2校验是否为恶意程序
	 * 3.份文件存储  文件.hash=32位~~2位
	 * 防止文件重名,自定义文件名称uudid;
	 */
	@Override
	public ImageVo upload(MultipartFile uploadFile) {
		
		//1.获取图片名称  abc.jpg
		String fileName = uploadFile.getOriginalFilename();
		fileName=fileName.toLowerCase();
		//2.校验正则表达式 \
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {

			return ImageVo.fail();
		}
		//3.校验恶意程序
		try {
		BufferedImage bImage = ImageIO.read(uploadFile.getInputStream());
			int width = bImage.getWidth();
			int height = bImage.getHeight();
			if(width ==0 || height == 0) {
				return ImageVo.fail();
			}
			//4.分文件存储
			String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
			String dirFilePath = localDirPath + dateDir;
			System.out.println(dirFilePath);
			File dirFile = new File(dirFilePath);
			if(!dirFile.exists()) {
				//入国文件不存在,需要创建目录
				dirFile.mkdirs();
			}
			//5.动态生成文件名称uuid+文件后缀
			String uuid = UUID.randomUUID().toString();
			//abc.jpg
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String realFileName = uuid + fileType;
			String realFilePath = dirFilePath+realFileName;
			uploadFile.transferTo(new File(realFilePath));
			//6.实现数据的回显
			String url = urlDirPath+dateDir+realFileName;
			ImageVo imageVo=new ImageVo(0,url,width,height );
			return imageVo;
		}catch(Exception e) {
			e.printStackTrace();
			return ImageVo.fail();
		}
	}

}
