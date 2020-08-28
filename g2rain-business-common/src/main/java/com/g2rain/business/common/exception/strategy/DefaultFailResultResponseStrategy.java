package com.g2rain.business.common.exception.strategy;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.result.BaseResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultFailResultResponseStrategy implements FailResultResponseStrategy {

	@Override
	public void write(HttpServletRequest request, HttpServletResponse response, BaseResult fail) {
		if (fail != null) {
			String pathInfo = request.getServletPath();
			fail.setPath(pathInfo);
			String method = request.getMethod();
			fail.setMethod(method);
			String requestId = request.getHeader("request_id");
			fail.setRequestId(requestId);
			fail.setTimestamp(new Date());
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				response.getWriter().print(JSONObject.toJSONString(fail));
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}
}
