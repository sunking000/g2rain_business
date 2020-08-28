package com.g2rain.business.common.domain;

import java.util.Set;

public class BaseSelectParam {
	private boolean deleteFlag;
	/**
	 * 偏移量
	 */
	private int pageNum = 1;
	/**
	 * 页容量
	 */
	private int pageSize = 10;

	private Set<String> keyIds;

	public BaseSelectParam() {

	}

	public BaseSelectParam(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		if (pageSize <= 0) {
			pageSize = 10;
		} else if (pageSize > 20000) {
			pageSize = 20000;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getOffset() {
		return (pageNum > 0 ? pageNum -1 : 0) * pageSize;
	}
	
	public int getSize() {
		return pageSize > 0 ? pageSize : 10;
	}

	public Set<String> getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(Set<String> keyIds) {
		this.keyIds = keyIds;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
