package com.g2rain.business.core.po;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.g2rain.business.common.domain.BaseSelectParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganSelectParam extends BaseSelectParam {
	private String organIds;
	private String type;
	private String status;
	private String organId;
	private String name;
	private String parentOrganId;
	/**
	 * 城市编码
	 */
	private String cityCode;
	private String code;
	private Boolean admin;
	/**
	 * 名称模糊查询
	 */
	private String namePattern;
	private String startTime;
	private String endTime;
	// 外部组织ID
	private String outOrganId;

	public OrganSelectParam() {
		super();
	}

	public List<String> getOrganIdList() {
		if(StringUtils.isNotBlank(organIds)) {
			String[] strings = organIds.split(",");
			List<String> organIdList = Arrays.asList(strings);
			return organIdList;
		}
		
		return null;
	}

	public String getNamePattern() {
		return namePattern == null ? null : "%" + namePattern + "%";
	}

}