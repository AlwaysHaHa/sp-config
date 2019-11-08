package com.jt.util;
/**
 *  统计java文件中的代码行数
 * @author tedu
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CodeCounterUtil {
	/**
	 *统计的 文件数量
	 */
	private static long files = 0;
	/**
	 * 代码行数
	 */
	private static int codeLines = 0;
	/**
	 * 注释行数
	 */
	private static int commentLines = 0;
	/**
	 * 空白行数
	 */
	private static int blankLines = 0;
	/**
	 * 文件数组
	 */
	private static ArrayList<File> fileArray = new ArrayList<>();
	
	/**
	 * 统计指定目录下(文件夹中)java文件的代码行数
	 * @param filePath 文件夹路径
	 * @return 代码总行数
	 */
	public static int getCodeNumFromFolder(String filePath) {
		String path = filePath.replace("target/test-classes", "src");
		
		ArrayList<File> al = getFile(new File(path));
		for (File file : al) {
			//匹配java格式的文件
			if (file.getName().matches(".*\\.java$")) {
				count(file);
			}
		}
		System.out.println("代码行数:"+codeLines);
		System.out.println("注释行数"+commentLines);
		System.out.println("空白行数:"+blankLines);
		int countAll = codeLines + commentLines + blankLines;
		return countAll;
	}


	/**
	 * 获得目录下的文件和子目录下的文件
	 * @param file
	 * @return
	 */

	private static ArrayList<File> getFile(File file) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File child : files) {
				if (child.isDirectory()) {
					getFile(child);
				}else {
					fileArray.add(child);
				}
			}
			
		}
		return fileArray;
	}
	/**
	 * 统计java文件中的代码行数
	 * @param file 具体的java文件
	 */
	private static void count(File file) {
		BufferedReader br = null;
		boolean flag = false;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				//除去注释前的空格
				line = line.trim();
				//匹配空行
				if (line.matches("^[ ]*$")) {
					blankLines++;
				}else if (line.startsWith("//")) {
					commentLines++;
				}else if (line.startsWith("/*") && !line.endsWith("*/")) {
					commentLines++;
					flag = true;
				}else if (line.startsWith("/*") && line.endsWith("*/")) {
					commentLines++;
				}else if(flag) {
					commentLines++;
					if (line.endsWith("*/")) {
						flag = false;
					}
				}else if (line.startsWith("/**") && !line.endsWith("**/")) {
					commentLines++;
					flag = true;
				}else if (line.startsWith("/**") && line.endsWith("**/")) {
					commentLines++;
				}else if(flag) {
					commentLines++;
					if (line.endsWith("**/")) {
						flag = false;
					}
				}else {
					codeLines++;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * 获取具体文件夹中的代码行数
	 * @param filePath
	 * @return
	 */
	public static int getCodeNumFromFile(String filePath1) {
		File fileName = new File(filePath1);
		if (fileName.getName().matches(".*\\.java$")) {
			count(fileName);
		}
		int codeNum = codeLines +blankLines + commentLines;
		System.out.println("代码总行数" + codeNum);
		return codeNum;
	}
	
}



































