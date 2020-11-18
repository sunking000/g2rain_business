package com.g2rain.business.common.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.g2rain.business.common.annotations.AutoFill;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.servlet.BufferedServletRequestWrapper;
import com.g2rain.business.common.utils.CommonContextContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoFillInterceptor extends HandlerInterceptorAdapter {

	public AutoFillInterceptor() {
	}

	public boolean isExclude(HttpServletRequest request) {
		return !CommonContextContainer.isRequestMappingFlag();
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.debug("AutoFillInterceptor");
		if (isExclude(request)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		AutoFill autoFillAnnotation = method.getAnnotation(AutoFill.class);

		if (!CommonContextContainer.isBackEnd() && autoFillAnnotation != null) {
			// 存在注解，并且不是管理员和代理商进行自动填充
			if (!CommonContextContainer.isAdminCompany()) {
				if (!(request instanceof BufferedServletRequestWrapper)) {
					log.error("request 类型错误，必须为BufferedServletRequestWrapper");
					throw new BussinessRuntimeException(ErrorCodeEnum.SERVICE_ERROR);
				}

				BufferedServletRequestWrapper bufferedServletRequestWrapper = (BufferedServletRequestWrapper) request;
				boolean userIdRequire = autoFillAnnotation.userIdRequire();
				boolean memberIdRequire = autoFillAnnotation.memberIdRequire();
				if (userIdRequire) {
					String userId = CommonContextContainer.getUserId();
					String parameterUserId = bufferedServletRequestWrapper.getParameter("userId");
					if (StringUtils.isBlank(parameterUserId)) {
						bufferedServletRequestWrapper.addParam("userId", userId);
					}
				}
				if (memberIdRequire) {
					String memberId = CommonContextContainer.getMemberId();
					String parameterMemberId = bufferedServletRequestWrapper.getParameter("memberId");
					if (StringUtils.isBlank(parameterMemberId)) {
						bufferedServletRequestWrapper.addParam("memberId", memberId);
					}
				}

				String organId = CommonContextContainer.getOrganId();
				boolean organIdRequire = autoFillAnnotation.organIdRequire();
				if (organIdRequire) {
					String parameterOrganId = bufferedServletRequestWrapper.getParameter("organId");
					if (StringUtils.isBlank(parameterOrganId)) {
						bufferedServletRequestWrapper.addParam("organId", organId);
					}
				}

				boolean storeOrganIdRequire = autoFillAnnotation.storeIdRequire();
				if (storeOrganIdRequire && CommonContextContainer.getOrganType().equals(OrganTypeEnum.STORE)) {
					if (StringUtils.isNotBlank(organId)) {
						bufferedServletRequestWrapper.addParam("storeOrganId", organId);
					}
				}
			}
		}
		
		return true;
	}
}
