package com.g2rain.business.common.exception.strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.g2rain.business.common.result.BaseResult;

public interface FailResultResponseStrategy {

	public void write(HttpServletRequest request, HttpServletResponse response, BaseResult failResult);
}