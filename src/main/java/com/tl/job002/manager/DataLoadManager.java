package com.tl.job002.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.tl.job002.pojos.UserAndContentInfoPojo;
import com.tl.job002.pojos.WbContentInfoPojo;
import com.tl.job002.pojos.WbUserInfoPojo;
import com.tl.job002.utils.DateUtil;
import com.tl.job002.utils.FileOperatorUtil;
import com.tl.job002.utils.IOUtil;
import com.tl.job002.utils.XmlParserUtil;
/**
 * 数据结构化处理类
 * @author dell
 *思路：1、原数据文件-->UserInfo()对象       -->list
 *                 -->ContentInfo()对象   -->list  --->UserAndContentInfoPojo对象
 *     2、UserAndContentInfoPojo对象   -->user_pojo_list.txt
 *                                   -->content_pojo_list.txt
 *                                   
 *思路整合：
 *数据文件： ->{uid,List<String>} ->{userInfoPojo}
 */
public class DataLoadManager {
	public static class UidAndListPojo {
		//内部静态
		//uid
		private String uid;
		//该uid对应的每条信息：
		private List<String> lineList;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public List<String> getLineList() {
			return lineList;
		}

		public void setLineList(List<String> lineList) {
			this.lineList = lineList;
		}

	}
/**
 * 得到所有源文件的
 * @param inputDir
 * @param charset
 * @return
 * @throws Exception
 */
	public static List<UidAndListPojo> getAllFileMapResult(String inputDir,
			String charset) throws Exception {
		// key是uid,value是行集合
		List<UidAndListPojo> uidAndListPojoList = new ArrayList<UidAndListPojo>();

		List<String> txtFilePathList = FileOperatorUtil
				.getAllSubNormalFilePath(inputDir);
		for (String txtFilePath : txtFilePathList) {
			ArrayList<String> txtLineList = new ArrayList<String>();
			List<String> singleTxtLineList = IOUtil.getTxtContent(txtFilePath,
					charset);
			txtLineList.addAll(singleTxtLineList);
			//获取该子文件的文件uid
			String uidValue = FileOperatorUtil
					.getFileNameWithoutSuffix(txtFilePath);

			UidAndListPojo uidAndListPojo = new UidAndListPojo();
			uidAndListPojo.setLineList(txtLineList);
			uidAndListPojo.setUid(uidValue);
			uidAndListPojoList.add(uidAndListPojo);
		}
		return uidAndListPojoList;
	}

	public static UserAndContentInfoPojo getConstructInfoPojo(
			List<UidAndListPojo> uidAndListPojoList) throws ParseException {
		List<WbUserInfoPojo> userPojoList = new ArrayList<WbUserInfoPojo>();
		List<WbContentInfoPojo> contentPojoList = new ArrayList<WbContentInfoPojo>();
		int errorLineCounter4Content = 0;
		int errorLineCounter4User = 0;
		for (UidAndListPojo uidAndListPojo : uidAndListPojoList) {
			String uidValue = uidAndListPojo.getUid();
			for (String line : uidAndListPojo.getLineList()) {
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				if (line.startsWith("<")) {
					// 说明content类型
					line = line.trim();
					//获取该元素的根元素
					Element rootElement = XmlParserUtil.getXmlRootElement(line,true);
					//出错过滤掉就可以
					if (rootElement == null) {
						// System.out.println("解析出现错误!");
//						System.out.println(line);
						errorLineCounter4Content++;
						continue;
					}
					WbContentInfoPojo contentInfoPojo = new WbContentInfoPojo();
					contentInfoPojo.setUid(Long.parseLong(uidValue));
					contentInfoPojo.setContent(rootElement
							.elementText("content"));
					contentInfoPojo.setTime(DateUtil.getDate(rootElement
							.elementText("time")));
					contentInfoPojo.setRepostsCount(Integer
							.parseInt(rootElement.elementText("repostsCount")));
					contentInfoPojo
							.setCommentsCount(Integer.parseInt(rootElement
									.elementText("commentsCount")));

					// 将形成的对象加入指定content List当中
					contentPojoList.add(contentInfoPojo);
				} else {
					// 剩余是user类型
					try {
						line = line.subSequence(line.indexOf('[') + 1,
								line.lastIndexOf(']')).toString();
						String[] kvArray = line.split(",");

						WbUserInfoPojo userInfoPojo = new WbUserInfoPojo();
						for (String kv : kvArray) {
							kv = kv.trim();
							String[] kvPair = kv.split("=");
							if (kvPair[0].equals("id")) {
								userInfoPojo.setUid(Long.parseLong(kvPair[1]));
							} else if (kvPair[0].equals("screenName")) {
								userInfoPojo.setScreenName(kvPair[1]);
							} else if (kvPair[0].equals("province")) {
								userInfoPojo.setProvince(Integer
										.parseInt(kvPair[1]));
							} else if (kvPair[0].equals("remark")) {
								userInfoPojo.setRemark(kvPair[1]);
							}
						}
						userPojoList.add(userInfoPojo);
					} catch (Exception e) {
//						System.out.println(line);
						errorLineCounter4User++;
					}
				}
			}
		}
		System.out.println("errorLineCounter4Content=" + errorLineCounter4Content);
		System.out.println("errorLineCounter4User=" + errorLineCounter4User);
		return new UserAndContentInfoPojo(userPojoList, contentPojoList);
	}
/**
 * 将
 * @param userAndContentInfoPojo
 * @param userOutputFilePath
 * @param contentOutputFilePath
 * @param outputCharset
 * @return
 * @throws Exception
 */
	public static boolean writePojoToFile(
			UserAndContentInfoPojo userAndContentInfoPojo,
			String userOutputFilePath, String contentOutputFilePath,
			String outputCharset) throws Exception {
		// 1、输出user pojo list
		List<WbUserInfoPojo> userInfoPojoList = userAndContentInfoPojo
				.getUserPojoList();
		StringBuilder stringBuilder = new StringBuilder();
		int lineCounter = 0;
		for (WbUserInfoPojo tempPojo : userInfoPojoList) {
			if (lineCounter > 0) {
				stringBuilder.append("\n");
			}
			stringBuilder.append(tempPojo.toString4FileOutput());
			lineCounter++;
		}
		IOUtil.writeListToFile(stringBuilder.toString(), userOutputFilePath,
				outputCharset);

		// 输出content pojo list
		List<WbContentInfoPojo> contentInfoPojoList = userAndContentInfoPojo
				.getContentPojoList();
		stringBuilder = new StringBuilder();
		lineCounter = 0;
		for (WbContentInfoPojo tempPojo : contentInfoPojoList) {
			if (lineCounter > 0) {
				stringBuilder.append("\n");
			}
			stringBuilder.append(tempPojo.toString4FileOutput());
			lineCounter++;
		}
		IOUtil.writeListToFile(stringBuilder.toString(), contentOutputFilePath,
				outputCharset);

		return true;
	}
/**
 * 标准的输入输出
 * @param inputDir
 * @param inputCharset
 * @param output4User
 * @param output4Content
 * @param outputCharset
 * @return
 */
	public static boolean startProcess(String inputDir, String inputCharset,
			String output4User, String output4Content, String outputCharset) {
		try {
			// 把给定目录中的文本文件读取成list
			List<UidAndListPojo> uidAndLiPojoList = getAllFileMapResult(
					inputDir, inputCharset);
			// 将字符串的list转化成结构化对象pojo形式的list
			UserAndContentInfoPojo userAndContentInfoPojo = getConstructInfoPojo(uidAndLiPojoList);
			// 把两个pojo形式的list对象，分别持久化输出到一个统一的文本文件中，编码为utf-8
			writePojoToFile(userAndContentInfoPojo, output4User,
					output4Content, outputCharset);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void main(String[] args) throws Exception {
		// String inputDir = "房地产";
		String inputDir = "weibodata";
		String inputCharset = "gbk";

		String output4User = "user_pojo_list.txt";
		String output4Content = "content_pojo_list.txt";
		String outputCharset = "utf-8";

		startProcess(inputDir, inputCharset, output4User, output4Content,
				outputCharset);

		System.out.println("done!");
	}
}
