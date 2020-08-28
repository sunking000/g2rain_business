package com.g2rain.business.common.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import com.g2rain.business.common.domain.Context;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.enums.RequestHeaderKeyEnum;
import com.g2rain.business.common.enums.SessionTypeEnum;

public class CommonContextContainer {
	private static ThreadLocal<Context> contextContainer = new ThreadLocal<Context>();
	
	public static Context getContext() {
		Context context = contextContainer.get();
		if (context == null) {
			context = new Context();
			contextContainer.set(context);
		}
		return context;
	}

	public static void setRequestId(String requestId) {
		Context context = getContext();
		context.setRequestId(requestId);
	}

	public static String getRequestId() {
		Context context = contextContainer.get();
		return context == null ? null : context.getRequestId();
	}

	public static void setRequestTime(String requestTime) {
		requestTime = DateFormatUtil.universalizeDatetime(requestTime);
		Context context = getContext();
		context.setRequestTime(requestTime);
	}

	public static String getRequestTime() {
		Context context = contextContainer.get();
		return context == null ? null : context.getRequestTime();
	}

	public static void setOrganId(String organId) {
		Context context = getContext();
		context.setOrganId(organId);
	}

	public static String getOrganId() {
		Context context = contextContainer.get();
		return context == null ? null : context.getOrganId();
	}

	public static void setUserId(String userId) {
		Context context = getContext();
		context.setUserId(userId);
	}

	public static String getUserId() {
		Context context = contextContainer.get();
		return context == null ? null : context.getUserId();
	}

	public static String getMemberId() {
		Context context = contextContainer.get();
		return context == null ? null : context.getMemberId();
	}

	public static void setMemberId(String memberId) {
		Context context = getContext();
		context.setMemberId(memberId);
	}

	public static void setAdminUser(boolean adminUser) {
		Context context = getContext();
		context.setAdminUser(adminUser);
	}

	public static boolean isAdminUser() {
		Context context = contextContainer.get();
		return context == null ? false : context.isAdminUser();
	}

	public static void setAdminCompany(boolean admin) {
		Context context = getContext();
		context.setAdminCompany(admin);
	}

	public static boolean isAdminCompany() {
		Context context = contextContainer.get();
		return context == null ? false : context.isAdminCompany();
	}

	public static void setSessionType(SessionTypeEnum sessionType) {
		Context context = getContext();
		context.setSessionType(sessionType);
	}

	public static SessionTypeEnum getSessionType() {
		Context context = contextContainer.get();
		return context == null ? null : context.getSessionType();
	}

	public static void setOrganType(OrganTypeEnum organType) {
		Context context = getContext();
		context.setOrganType(organType);
	}

	public static OrganTypeEnum getOrganType() {
		Context context = contextContainer.get();
		return context == null ? null : context.getOrganType();
	}

	public static void setRequestMappingFlag(boolean requestMappingFlag) {
		Context context = getContext();
		context.setRequestMappingFlag(requestMappingFlag);
	}

	public static boolean isRequestMappingFlag() {
		Context context = contextContainer.get();
		return context == null ? false : context.isRequestMappingFlag();
	}
	
	public static boolean isBackEnd() {
		Context context = contextContainer.get();
		return context == null ? false : context.isBackEnd();
	}
	
	public static void setBackEnd(boolean backEnd) {
		Context context = getContext();
		context.setBackEnd(backEnd);
	}
	
	public static boolean isDebugFlag() {
		Context context = contextContainer.get();
		return context == null ? false : context.isDebugFlag();
	}
	
	public static void setDebugFlag(boolean debugFlag) {
		if (debugFlag) {
			MDC.put("DEBUG_LOG", "TRUE");
		}
		Context context = getContext();
		context.setDebugFlag(debugFlag);
	}

	public static void setHoldStoreOrganIds(String holdStoreOrganIds) {
		Context context = getContext();
		context.setHoldStoreOrganIds(holdStoreOrganIds);
	}

	public static String getHoldStoreOrganIds() {
		Context context = contextContainer.get();
		return context == null ? null : context.getHoldStoreOrganIds();
	}

	public static MultiValueMap<String, String> getHeaders() {
		MultiValueMap<String, String> items = new HttpHeaders();
		items.add(RequestHeaderKeyEnum.REQUEST_ID.getUpper(), getRequestId());
		items.add(RequestHeaderKeyEnum.REQUEST_TIME.getUpper(), getRequestTime());
		items.add(RequestHeaderKeyEnum.SESSION_TYPE.getUpper(), getSessionType().name());
		items.add(RequestHeaderKeyEnum.ADMIN_USER.getUpper(), isAdminUser() + "");
		items.add(RequestHeaderKeyEnum.ADMIN_COMPANY.getUpper(), isAdminCompany() + "");
		items.add(RequestHeaderKeyEnum.DEBUG_FLAG.getUpper(), isDebugFlag() + "");
		items.add(RequestHeaderKeyEnum.BACK_EDN.getUpper(), isBackEnd() + "");
		if (StringUtils.isNotBlank(getUserId())) {
			items.add(RequestHeaderKeyEnum.USER_ID.getUpper(), getUserId());
		}
		if (StringUtils.isNotBlank(getMemberId())) {
			items.add(RequestHeaderKeyEnum.MEMBER_ID.getUpper(), getMemberId());
		}

		return items;
	}

	public static String getContextString() {
		Context context = contextContainer.get();
		return context == null ? null : context.toString();
	}

	public static void remove() {
		MDC.clear();
		if (contextContainer != null) {
			contextContainer.remove();
		}
	}
}
