package com.g2rain.business.core.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.utils.OrganStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganVo extends BaseVo {

	private String organId;
	/**
	 * 类型
	 */
	@NotNull
	private OrganTypeEnum type;
	@NotNull
	private String name;
	/**
	 * 外部组织结构id
	 */
	private String outOrganId;
	/**
	 * 是否为管理工
	 */
	private Boolean admin = false;
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
	private OrganStatusEnum status;

	public String getTypeName() {
		if (type != null) {
			return type.typeName;
		}
		
		return null;
	}

	public OrganVo() {
		super();
	}

	public OrganVo(OrganPo organ) {
		super(organ);
		BeanUtils.copyProperties(organ, this);
		if (StringUtils.isNotBlank(organ.getStatus())) {
			this.status = OrganStatusEnum.valueOf(organ.getStatus());
		}
		if (StringUtils.isNotBlank(organ.getType())) {
			this.type = OrganTypeEnum.valueOf(organ.getType());
		}
	}
}
