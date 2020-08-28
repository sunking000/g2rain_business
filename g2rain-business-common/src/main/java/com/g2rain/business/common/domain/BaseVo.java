package com.g2rain.business.common.domain;

import com.g2rain.business.common.utils.DateFormatUtil;

public class BaseVo {
	private long id;
	private String createTime;
	private String updateTime;
	private int version;
	private boolean deleteFlag;

	public BaseVo() {

	}

	public BaseVo(BasePo basePo) {
		this.id = basePo.getId();
		if (basePo.getCreateTime() != null) {
			this.createTime = DateFormatUtil.format(basePo.getCreateTime());
		}
		if (basePo.getUpdateTime() != null) {
			this.updateTime = DateFormatUtil.format(basePo.getUpdateTime());
		}
		this.version = basePo.getVersion();
		this.deleteFlag = basePo.isDeleteFlag();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
