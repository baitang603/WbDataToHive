package com.tl.job002.pojos;

import java.util.List;
/**
 * 将User集合和content集合封装到一起
 * @author dell
 *
 */
public class UserAndContentInfoPojo {
	private List<WbUserInfoPojo> userPojoList;
	private List<WbContentInfoPojo> contentPojoList;
	public UserAndContentInfoPojo(List<WbUserInfoPojo> userPojoList,
			List<WbContentInfoPojo> contentPojoList) {
		super();
		this.userPojoList = userPojoList;
		this.contentPojoList = contentPojoList;
	}
	public List<WbUserInfoPojo> getUserPojoList() {
		return userPojoList;
	}
	public void setUserPojoList(List<WbUserInfoPojo> userPojoList) {
		this.userPojoList = userPojoList;
	}
	public List<WbContentInfoPojo> getContentPojoList() {
		return contentPojoList;
	}
	public void setContentPojoList(List<WbContentInfoPojo> contentPojoList) {
		this.contentPojoList = contentPojoList;
	}
}
