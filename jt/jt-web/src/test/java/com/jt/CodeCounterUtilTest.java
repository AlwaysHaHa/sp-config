package com.jt;

import org.junit.Test;

import com.jt.util.CodeCounterUtil;

public class CodeCounterUtilTest {
	@Test
	public void testCodeCount() {
		int codeNum = CodeCounterUtil.getCodeNumFromFolder("D:\\jt-software\\jt");
		System.out.println(codeNum);
		
	}
}
