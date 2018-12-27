package com.tl.job002.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
	/**
	 * 文件按行读到list
	 * @param txtFilePath
	 * @param charset
	 * @return
	 * @throws Exception
	 */
		public static List<String> getTxtContent(String txtFilePath, String charset)
			throws Exception {
		File txtFile = new File(txtFilePath);
		FileInputStream fis = new FileInputStream(txtFile);
		InputStreamReader isr = new InputStreamReader(fis, charset);
		BufferedReader br = new BufferedReader(isr);

		List<String> lineList = new ArrayList<String>();
		String tempLine = null;
       //将读取的每一行放入list<String>中
		//即每一行comment都是一个List对象
		while ((tempLine = br.readLine()) != null) {
			lineList.add(tempLine);
		}
		br.close();
		return lineList;
	}
/**
 * 
 * @param lineList
 * @param outputFilePath
 * @param charset
 * @return
 * @throws Exception
 */
	public static boolean writeListToFile(List<String> lineList,
			String outputFilePath, String charset) throws Exception {
		File outputFile = new File(outputFilePath);
		FileOutputStream fos = new FileOutputStream(outputFile);
		int lineCounter = 0;
		for (String line : lineList) {
			if (lineCounter > 0) {
				//先判断下一行有内容，在输出换行符，防止输出内容就立刻输出换行符导致最后多一个换行符
				//第一行不会输出，当第二行才会输出，最后一行也不会输出
				fos.write('\n');
			}
			
			fos.write(line.getBytes(charset));
			lineCounter++;
		}
		fos.close();
		return true;
	}

	public static boolean writeListToFile(String txtContent,
			String outputFilePath, String charset) throws Exception {
		File outputFile = new File(outputFilePath);
		FileOutputStream fos = new FileOutputStream(outputFile);
		fos.write(txtContent.getBytes(charset));
		fos.close();
		return true;
	}

	public static void main(String[] args) throws Exception {
		// String txtFilePath = "房地产\\user\\2297199692.txt";
		String txtFilePath = "房地产\\content\\1484018951.txt";
		String inputCharset = "gbk";
		String outputCharset = "utf-8";
		String outputFilePath = "newFile.txt";
		List<String> lineList = getTxtContent(txtFilePath, inputCharset);
		for (String tempLine : lineList) {
			System.out.println(tempLine);
		}
		writeListToFile(lineList, outputFilePath, outputCharset);

	}
}
