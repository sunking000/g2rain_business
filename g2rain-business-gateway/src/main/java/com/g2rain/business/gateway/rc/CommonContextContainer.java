package com.g2rain.business.gateway.rc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.server.ServerWebExchange;

public class CommonContextContainer {
	private static final String CONTEXT_NAME = "context";

	public static Context getContext(ServerWebExchange exchange) {
		Object context = exchange.getAttribute(CONTEXT_NAME);
		if (context == null) {
			context = new Context();
			exchange.getAttributes().put(CONTEXT_NAME, context);
		}
		return (Context) context;
	}

	public static Map<String, String> getHeaders(Context context) {
		Map<String, String> items = new HashMap<String, String>();
		items.put(CustomizeHeaderKeyEnum.REQUEST_ID.getUpper(), context.getRequestId());
		items.put(CustomizeHeaderKeyEnum.REQUEST_TIME.getUpper(), context.getRequestTime());
		items.put(CustomizeHeaderKeyEnum.SESSION_TYPE.getUpper(), context.getSessionType());
		items.put(CustomizeHeaderKeyEnum.ADMIN_USER.getUpper(), context.isAdminUser() + "");
		items.put(CustomizeHeaderKeyEnum.ADMIN_COMPANY.getUpper(), context.isAdminCompany() + "");
		items.put(CustomizeHeaderKeyEnum.DEBUG_FLAG.getUpper(), context.isDebugFlag() + "");
		if (StringUtils.isNotBlank(context.getUserId())) {
			items.put(CustomizeHeaderKeyEnum.USER_ID.getUpper(), context.getUserId());
		}
		if (StringUtils.isNotBlank(context.getOrganId())) {
			items.put(CustomizeHeaderKeyEnum.ORGAN_ID.getUpper(), context.getOrganId());
		}

		if (StringUtils.isNotBlank(context.getMemberId())) {
			items.put(CustomizeHeaderKeyEnum.MEMBER_ID.getUpper(), context.getMemberId());
		}
		if (context.getLocale() != null) {
			Locale locale = context.getLocale();
			items.put("Accept-Language", locale.getLanguage());
		}
		return items;
	}

	public static void removeContext(ServerWebExchange exchange) {
		Object context = exchange.getAttribute(CONTEXT_NAME);
		if (context != null) {
			exchange.getAttributes().remove(CONTEXT_NAME);
		}
	}
}
