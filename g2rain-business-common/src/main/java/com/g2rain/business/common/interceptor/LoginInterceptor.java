package com.g2rain.business.common.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.g2rain.business.common.annotations.Login;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.SessionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.utils.CommonContextContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	public LoginInterceptor() {
	}

	public boolean isExclude(HttpServletRequest request) {
		return !CommonContextContainer.isRequestMappingFlag();
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.debug("LoginInterceptor");

		if (isExclude(request)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Login loginAnnotation = method.getAnnotation(Login.class);
		boolean requireLogin = true;
		if (loginAnnotation != null) {
			requireLogin = loginAnnotation.require();
		}

		if (!CommonContextContainer.isBackEnd() && requireLogin) {
			// 需要登陆
			String userId = CommonContextContainer.getUserId();
			String memberId = CommonContextContainer.getMemberId();
			if (CommonContextContainer.getSessionType().equals(SessionTypeEnum.USER)
					&& StringUtils.isNotBlank(userId)) {
				return true;
			} else if (CommonContextContainer.getSessionType().equals(SessionTypeEnum.MEMBER)
					&& StringUtils.isNotBlank(memberId)) {
				return true;
			} else {
				log.error("登陆状态不正确,userId:{}, memberId:{}, sessionType:{},", userId, memberId,
						CommonContextContainer.getSessionType());
				throw new BussinessRuntimeException(ErrorCodeEnum.USER_NOT_LOGIN.name());
			}
		} else {
			// 不需要登陆 通过校验
			log.debug("不需要登录");
			return true;
		}
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		CommonContextContainer.remove();
	}
}
