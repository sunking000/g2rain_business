package com.g2rain.business.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.AuthorityPermissionRoleBo;
import com.g2rain.business.core.po.AuthorityPermissionRoleSelectParam;
import com.g2rain.business.core.vo.AuthorityPermissionRoleBatchParam;
import com.g2rain.business.core.vo.AuthorityPermissionRoleVo;

@RequestMapping("authority_role_permission")
@Controller
public class AuthorityPermissionRoleController {

	@Autowired
	private AuthorityPermissionRoleBo authorityPermissionRoleBo;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<AuthorityPermissionRoleVo> addOrEdit(@RequestBody AuthorityPermissionRoleVo vo) {
		AuthorityPermissionRoleVo authorityPermissionRoleVo = authorityPermissionRoleBo.addOrEdit(vo);
		SpecificResult<AuthorityPermissionRoleVo> success = new SpecificResult<AuthorityPermissionRoleVo>(
				BaseResult.SUCCESS);
		success.setResultData(authorityPermissionRoleVo);
		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "batch_add_delete")
	public BaseResult newBatchAddOrUpdate(@RequestBody AuthorityPermissionRoleBatchParam authorityPermissionRoleBatchVo) {
		Map<String, Integer> map = authorityPermissionRoleBo.batchAddOrUpdate(authorityPermissionRoleBatchVo);
		SpecificResult<Map<String, Integer>> success = new SpecificResult<Map<String, Integer>>(BaseResult.SUCCESS);
		success.setResultData(map);

		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<AuthorityPermissionRoleVo>> list(
			AuthorityPermissionRoleSelectParam param) {
		SpecificPageInfoResult<AuthorityPermissionRoleVo> specificPageInfoResult = authorityPermissionRoleBo
				.list(param);
		SpecificResult<SpecificPageInfoResult<AuthorityPermissionRoleVo>> result = new SpecificResult<>(
				BaseResult.SUCCESS);
		result.setResultData(specificPageInfoResult);

		return result;
	}
}