package com.g2rain.business.common.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasePo {
	private long id;
	private Date createTime;
	private Date updateTime;
	private int version;
	private boolean deleteFlag;

	public BasePo() {
		super();
	}

	public BasePo(BaseVo baseVo) {
		super();
	}
}
