package com.g2rain.business.common.domain;

import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.enums.SessionTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Context {
	private String requestId;
	private String requestTime;
	private String organId;
	private String userId;
	private String memberId;
	private boolean adminUser;
	private boolean adminCompany;
	private SessionTypeEnum sessionType;
	private OrganTypeEnum organType;
	private boolean requestMappingFlag;
	private boolean backEnd;
	private boolean debugFlag;
	private String holdStoreOrganIds;
}
