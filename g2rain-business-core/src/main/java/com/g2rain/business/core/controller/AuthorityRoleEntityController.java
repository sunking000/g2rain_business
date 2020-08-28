package com.g2rain.business.core.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.AuthorityRoleEntityBo;
import com.g2rain.business.core.po.AuthorityRoleEntitySelectParam;
import com.g2rain.business.core.vo.AuthorityRoleEntityBatchParam;
import com.g2rain.business.core.vo.AuthorityRoleEntityVo;

@RequestMapping("authority_role_entity")
@Controller
public class AuthorityRoleEntityController {
	@Autowired
	private AuthorityRoleEntityBo authorityRoleEntityBo;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<AuthorityRoleEntityVo> add(@Valid @RequestBody AuthorityRoleEntityVo vo) {
		AuthorityRoleEntityVo authorityRoleEntityVo = authorityRoleEntityBo.add(vo);
		SpecificResult<AuthorityRoleEntityVo> result = new SpecificResult<>(BaseResult.SUCCESS);

		result.setResultData(authorityRoleEntityVo);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<AuthorityRoleEntityVo>> list(AuthorityRoleEntitySelectParam param) {
		SpecificPageInfoResult<AuthorityRoleEntityVo> specificPageInfoResult = authorityRoleEntityBo.list(param);
		SpecificResult<SpecificPageInfoResult<AuthorityRoleEntityVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(specificPageInfoResult);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "batch_add_delete")
	public SpecificResult<Map<String, Integer>> newBatchAddOrUpdate(
			@RequestBody AuthorityRoleEntityBatchParam authorityRoleEntityBatchVo) {
		Map<String, Integer> resultMap = authorityRoleEntityBo.batchAddAndDelete(authorityRoleEntityBatchVo);
		SpecificResult<Map<String, Integer>> success = new SpecificResult<>(BaseResult.SUCCESS);
		success.setResultData(resultMap);

		return success;
	}
}
