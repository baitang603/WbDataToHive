package com.tl.job002.utils;

import java.util.List;

public class StringUtil {
	public static String join(List<Object> objList, String deli) {
		//记录当前行号
		int lineCounter = 0;
		//创建字符串
		StringBuilder stringBuilder = new StringBuilder();
		//遍历
		for (Object obj : objList) {
			if (lineCounter > 0) {
				//当不是第一行时候，进行拼接字符
				stringBuilder.append(deli);
			}
			stringBuilder.append(obj.toString());
			lineCounter++;
		}
		return stringBuilder.toString();
	}
}
