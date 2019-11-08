package com.jt.thro;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice			//对Controller层异常有效
@Slf4j
public class SystemExecution {
	
	//只对运行时有效
	@ExceptionHandler(RuntimeException.class)
	public SysResult exception(Throwable throwable) {
		throwable.printStackTrace();
		log.info(throwable.getMessage());
		return SysResult.fail("后台服务器调用失败");
	}
}
