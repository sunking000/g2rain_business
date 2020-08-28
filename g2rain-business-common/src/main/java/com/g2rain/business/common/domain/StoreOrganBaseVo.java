package com.g2rain.business.common.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreOrganBaseVo extends BaseVo {
	private String storeOrganId;

	public StoreOrganBaseVo() {
		super();
	}

	public StoreOrganBaseVo(StoreOrganBasePo storeOrganPo) {
		super(storeOrganPo);
	}
}
