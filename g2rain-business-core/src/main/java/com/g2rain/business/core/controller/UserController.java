package com.g2rain.business.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.annotations.AutoFill;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.UserBo;
import com.g2rain.business.core.po.UserQueryParam;
import com.g2rain.business.core.po.UserSelectParam;
import com.g2rain.business.core.vo.AddUserParam;
import com.g2rain.business.core.vo.UserUpdatePwdParam;
import com.g2rain.business.core.vo.UserVo;

@RequestMapping("user")
@Controller
public class UserController {

	@Autowired
	private UserBo userBo;
	
	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	public SpecificResult<Integer> update(@RequestBody UserVo user) {
		SpecificResult<Integer> success = new SpecificResult<>(BaseResult.SUCCESS);
		int updateRowCount = userBo.update(user);
		success.setResultData(updateRowCount);
		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public SpecificResult<Integer> delete(@PathVariable("userId") String userId) {
		int deleteRowCount = userBo.delete(userId);
		SpecificResult<Integer> success = new SpecificResult<>(BaseResult.SUCCESS);
		success.setResultData(deleteRowCount);
		return success;
	}

	@AutoFill(userIdRequire=true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "update_password")
	public SpecificResult<Integer> updatePwd(@RequestBody UserUpdatePwdParam userUpdatePwdParam) {
		int updateRowCount = userBo.updatePwd(userUpdatePwdParam);
		SpecificResult<Integer> success = new SpecificResult<>(BaseResult.SUCCESS);
		success.setResultData(updateRowCount);
		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<UserVo>> list(UserSelectParam param) {
		SpecificPageInfoResult<UserVo> specificPageInfoResult = userBo.list(param);
		SpecificResult<SpecificPageInfoResult<UserVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(specificPageInfoResult);
		return result;
	}

	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<Integer> add(@RequestBody AddUserParam user) {
		int insertRowCount = userBo.add(user);
		SpecificResult<Integer> success = new SpecificResult<>(BaseResult.SUCCESS);
		success.setResultData(insertRowCount);
		return success;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, path = "/{userId}")
	public SpecificResult<UserVo> get(@PathVariable String userId) {
		UserVo user = userBo.get(userId);

		SpecificResult<UserVo> success = new SpecificResult<UserVo>(BaseResult.SUCCESS);
		success.setResultData(user);

		return success;
	}

	@AutoFill(organIdRequire = true)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, path = "/mobile")
	public BaseResult getByMobile(UserQueryParam userQueryParam) {
		UserVo userVo = userBo.getByMobile(userQueryParam);
		SpecificResult<UserVo> success = new SpecificResult<UserVo>(BaseResult.SUCCESS);
		success.setResultData(userVo);

		return success;
	}
}
