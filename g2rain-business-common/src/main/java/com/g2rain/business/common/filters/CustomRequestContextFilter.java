package com.g2rain.business.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.g2rain.business.common.bo.ServletPathPatternMatcher;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.enums.RequestHeaderKeyEnum;
import com.g2rain.business.common.enums.SessionTypeEnum;
import com.g2rain.business.common.utils.CommonContextContainer;
import com.g2rain.business.common.utils.RequestHeaderUtil;

public class CustomRequestContextFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(CustomRequestContextFilter.class);

	@Autowired
	private ServletPathPatternMatcher servletPathPatternMatcher;

	public CustomRequestContextFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("RequestContextFilter init");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if (isExclude(request)) {
			chain.doFilter(request, response);
			return;
		}

		CommonContextContainer.setRequestMappingFlag(true);
		// String sessionId = request.getHeader("X-POS-SESSION-ID") == null ?
		// request.getHeader("x-pos-session-id")
		// : request.getHeader("X-POS-SESSION-ID");
		// if (StringUtils.isBlank(sessionId)) {
		// sessionId = request.getHeader("sessionId");
		// }
		// logger.debug("sessionId:{}", sessionId);
		// CommonContextContainer.setSessionId(sessionId);
		//
		String sessionType = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.SESSION_TYPE);
		logger.debug("sessionType:{}", sessionType);
		CommonContextContainer.setSessionType(sessionType != null ? SessionTypeEnum.valueOf(sessionType) : null);

		String debugFlag = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.DEBUG_FLAG);
		logger.debug("debugFlag:{}", debugFlag);
		CommonContextContainer.setDebugFlag(debugFlag == null ? false : Boolean.parseBoolean(debugFlag));

		String backEndFlag = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.BACK_EDN);
		logger.debug("backEndFlag:{}", backEndFlag);
		CommonContextContainer.setBackEnd(backEndFlag == null ? false : Boolean.parseBoolean(backEndFlag));

		String requestId = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.REQUEST_ID);
		logger.debug("requestId:{}", requestId);
		CommonContextContainer.setRequestId(requestId);

		String requestTime = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.REQUEST_TIME);
		logger.debug("requestTime:{}", requestTime);
		CommonContextContainer.setRequestTime(requestTime);

		String userId = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.USER_ID);
		logger.debug("userId:{}", userId);
		CommonContextContainer.setUserId(userId);

		String memberId = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.MEMBER_ID);
		logger.debug("memberId:{}", memberId);
		CommonContextContainer.setMemberId(memberId);

		String adminUser = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.ADMIN_USER);
		logger.debug("adminUser:{}", adminUser);
		CommonContextContainer.setAdminUser(adminUser == null ? false : Boolean.parseBoolean(adminUser));

		String adminCompany = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.ADMIN_COMPANY);
		logger.debug("adminCompany:{}", adminCompany);
		CommonContextContainer.setAdminCompany(adminCompany == null ? false : Boolean.parseBoolean(adminCompany));

		String organType = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.ORGAN_TYPE);
		logger.debug("organType:{}", organType);
		CommonContextContainer.setOrganType(organType == null ? null : OrganTypeEnum.valueOf(organType));

		String organId = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.ORGAN_ID);
		logger.debug("organId:{}", organId);
		CommonContextContainer.setOrganId(organId);

		String holdStoreIds = RequestHeaderUtil.getHeaderValue(request, RequestHeaderKeyEnum.HOLD_STORE_ORGAN_IDS);
		logger.debug("holdStoreIds:{}", holdStoreIds);
		CommonContextContainer.setHoldStoreOrganIds(holdStoreIds);

		chain.doFilter(request, response);
		CommonContextContainer.remove();
	}

	@Override
	public void destroy() {

	}

	public boolean isExclude(HttpServletRequest request) {
		return !servletPathPatternMatcher.isMatch(request);
	}
}
