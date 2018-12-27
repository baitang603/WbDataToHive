package com.tl.job002.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOperatorUtil {
	public static List<String> getAllSubNormalFilePath(String filePath) {
		File file = new File(filePath);
		List<String> resultList = new ArrayList<String>();
		// 如果是目录，则往下一层
		if (file.isDirectory()) {
			for (File tempFile : file.listFiles()) {
				//将该直接子文件直接写到list中
				//注意：这里使用递归的方式，返回一级目录的全路径
				resultList.addAll(getAllSubNormalFilePath(tempFile.toString()));
			}
		} else {
			resultList.add(file.toString());
		}
		return resultList;
	}
	/**
	 * 得到不带后缀的文件名
	 * @param inputPath
	 * @return
	 */
	public static String getFileNameWithoutSuffix(String inputPath){
		//new File(inputPath).getName().split("\\.")
		
		return new File(inputPath).getName().split("\\.")[0];
	}
	public static void main(String[] args) {
		String inputPath="房地产\\user\\1855569733.txt";
		System.out.println();
	}
}
