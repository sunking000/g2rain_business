package com.g2rain.business.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.annotations.AutoFill;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.AuthorityRoleUserBo;
import com.g2rain.business.core.po.AuthorityRoleUserSelectParam;
import com.g2rain.business.core.vo.AddAuthorityRoleUserParam;
import com.g2rain.business.core.vo.AuthorityRoleUserBatchParam;
import com.g2rain.business.core.vo.AuthorityRoleUserVo;

@RequestMapping("authority_role_user")
@Controller
public class AuthorityRoleUserController {
	@Autowired
	private AuthorityRoleUserBo autorityRoleUserBo;

	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<Integer> add(@RequestBody AddAuthorityRoleUserParam param) {
		int insertRowCount = autorityRoleUserBo.add(param);
		// int insertRowCount = authorityRoleUserMapper.insert(po);
		// success.putData("insertRowCount", insertRowCount);
		SpecificResult<Integer> success = new SpecificResult<Integer>(BaseResult.SUCCESS);
		success.setResultData(insertRowCount);

		return success;
	}

	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	public SpecificResult<Integer> update(@RequestParam String roleId, @RequestParam String userId) {
		int deleteRowCount = autorityRoleUserBo.delete(roleId, userId);

		SpecificResult<Integer> success = new SpecificResult<Integer>(BaseResult.SUCCESS);
		success.setResultData(deleteRowCount);

		return success;
	}

	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "batch_add_delete")
	public SpecificResult<Map<String, Integer>> batchAddOrUpdate(
			@RequestBody AuthorityRoleUserBatchParam authorityRoleUserBatch) {
		Map<String, Integer> resultMap = autorityRoleUserBo.batchAddOrUpdate(authorityRoleUserBatch);
		SpecificResult<Map<String, Integer>> success = new SpecificResult<>(BaseResult.SUCCESS);
		success.setResultData(resultMap);

		return success;
	}

	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<AuthorityRoleUserVo>> list(AuthorityRoleUserSelectParam param) {
		SpecificPageInfoResult<AuthorityRoleUserVo> specificPageInfoResult = autorityRoleUserBo.list(param);

		SpecificResult<SpecificPageInfoResult<AuthorityRoleUserVo>> success = new SpecificResult<>(BaseResult.SUCCESS);
		success.setResultData(specificPageInfoResult);

		return success;
	}
}
