package com.g2rain.business.core.po;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.core.vo.OrganVo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrganPo extends BasePo {
	private String organId;
	/**
	 * 类型
	 */
	private String type;
	private String name;
	/**
	 * 是否为管理工
	 */
	private Boolean admin = false;

	/**
	 * 外部组织结构id
	 */
	private String outOrganId;
	/**
	 * 城市编码
	 */
	private String cityCode;
	/**
	 * 维度
	 */
	private BigDecimal latitude;
	/**
	 * 经度
	 */
	private BigDecimal longitude;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 状态
	 */
	private String status;

	public OrganPo() {

	}

	public OrganPo(OrganVo organ) {
		super();
		BeanUtils.copyProperties(organ, this);
		if (organ.getStatus() != null) {
			this.status = organ.getStatus().name();
		}
		if (organ.getType() != null) {
			this.type = organ.getType().name();
		}
	}
}