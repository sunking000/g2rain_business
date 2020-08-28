package com.g2rain.business.core.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.AuthorityRoleBo;
import com.g2rain.business.core.po.AuthorityRoleSelectParam;
import com.g2rain.business.core.vo.AuthorityRoleVo;

@RequestMapping("authority_role")
@Controller
public class AuthorityRoleController {
	@Autowired
	private AuthorityRoleBo autorityRoleBo;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<AuthorityRoleVo> add(@RequestBody @Valid AuthorityRoleVo authorityRole) {
		SpecificResult<AuthorityRoleVo> success = new SpecificResult<AuthorityRoleVo>(BaseResult.SUCCESS);
		AuthorityRoleVo authorityRoleVo = autorityRoleBo.add(authorityRole);
		success.setResultData(authorityRoleVo);
		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value = "update")
	public SpecificResult<Integer> update(@RequestBody AuthorityRoleVo authorityLink) {
		int updateRowCount = autorityRoleBo.update(authorityLink);
		SpecificResult<Integer> seccess = new SpecificResult<>(BaseResult.SUCCESS);
		seccess.setResultData(updateRowCount);

		return seccess;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<AuthorityRoleVo>> list(AuthorityRoleSelectParam param) {
		SpecificResult<SpecificPageInfoResult<AuthorityRoleVo>> success = new SpecificResult<>(BaseResult.SUCCESS);
		SpecificPageInfoResult<AuthorityRoleVo> specificPageInfoResult = autorityRoleBo.list(param);
		success.setResultData(specificPageInfoResult);
		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, path = "/{roleId}")
	public SpecificResult<Integer> delete(@PathVariable("roleId") String roleId) {
		int deleteRowCount = autorityRoleBo.delete(roleId);
		SpecificResult<Integer> success = new SpecificResult<Integer>(BaseResult.SUCCESS);
		success.setResultData(deleteRowCount);

		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/role_id")
	public SpecificResult<List<AuthorityRoleVo>> listByRoleId(@RequestParam String roleId) {
		List<AuthorityRoleVo> authorityLinkVos = autorityRoleBo.listByRoleId(roleId);
		SpecificResult<List<AuthorityRoleVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(authorityLinkVos);
		return result;
	}
}
