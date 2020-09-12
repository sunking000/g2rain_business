package com.g2rain.business.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.annotations.AutoFill;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.AuthorityBo;
import com.g2rain.business.core.utils.AuthorityPointTypeEnum;
import com.g2rain.business.core.vo.AuthorityVo;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("authority")
public class AuthorityController {

	@Autowired
	private AuthorityBo authorityBo;

	@AutoFill(userIdRequire = true)
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public SpecificResult<AuthorityVo> getAuthority(@RequestParam("userId") String userId,
			@RequestParam(value = "type", required = false) AuthorityPointTypeEnum type) {
		AuthorityVo authority = authorityBo.getAuthority(userId, type);
		SpecificResult<AuthorityVo> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(authority);

		return result;
	}
}
