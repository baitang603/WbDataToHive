package com.tl.job002.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tl.job002.utils.DateUtil;
import com.tl.job002.utils.StringUtil;

public class WbContentInfoPojo {
	private long uid;
	private String content;
	private Date time;
	private int repostsCount;
	private int commentsCount;
	/**
	 * 为了输出到文件，可以写一个指定输出格式的方法
	 * 目的：让对象以指定字符串格式输出到文件中
	 */
	public String toString4FileOutput() {
		List<Object> fieldList = new ArrayList<Object>();
		fieldList.add(uid);
		fieldList.add(content);
		fieldList.add(DateUtil.formatDate(time));
		fieldList.add(repostsCount);
		fieldList.add(commentsCount);
		//将List按照指定分隔符进行拼接
		return StringUtil.join(fieldList, "\001");
	}
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getRepostsCount() {
		return repostsCount;
	}
	public void setRepostsCount(int repostsCount) {
		this.repostsCount = repostsCount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
}
