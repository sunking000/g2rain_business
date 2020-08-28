package com.g2rain.business.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.annotations.Login;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.UserLoginBo;
import com.g2rain.business.core.vo.LoginToken;
import com.g2rain.business.core.vo.UserLoginParam;

@RequestMapping("user_auth")
@Controller
public class UserLoginController {

	@Autowired
	private UserLoginBo userLoginBo;

	@Login(require = false)
	@RequestMapping(method = RequestMethod.GET, value = "login_token")
	@ResponseBody
	public SpecificResult<LoginToken> getLoginToken(String accessKey) {
		LoginToken loginToken = userLoginBo.getLoginToken(accessKey);
		SpecificResult<LoginToken> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(loginToken);
		return result;
	}

	@Login(require = false)
	@RequestMapping(method = RequestMethod.POST, value="login")
	@ResponseBody
	public SpecificResult<LoginToken> login(@RequestBody UserLoginParam param) {
		LoginToken loginToken = userLoginBo.login(param);
		SpecificResult<LoginToken> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(loginToken);
		return result;
	}

	@Login(require = false)
	@RequestMapping(method = RequestMethod.GET, value = "/logout")
	@ResponseBody
	public BaseResult logout(String accessKey, HttpServletRequest request) {
		userLoginBo.logout(accessKey);
		return new BaseResult(BaseResult.SUCCESS);
	}
}