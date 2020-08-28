package com.g2rain.business.core.controller;

import java.util.List;

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
import com.g2rain.business.core.bo.AuthorityDictBo;
import com.g2rain.business.core.po.AuthorityDictSelectParam;
import com.g2rain.business.core.vo.AuthorityDictUserParam;
import com.g2rain.business.core.vo.AuthorityDictVo;

@RequestMapping("authority_dict")
@Controller
public class AuthorityDictController {

	@Autowired
	private AuthorityDictBo authorityDictBo;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<AuthorityDictVo> add(@RequestBody AuthorityDictVo vo) {
		AuthorityDictVo authorityDictVo = authorityDictBo.add(vo);
		SpecificResult<AuthorityDictVo> result = new SpecificResult<AuthorityDictVo>();
		result.setResultData(authorityDictVo);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value = "update")
	public SpecificResult<Integer> update(@RequestBody AuthorityDictVo vo) {
		int updateRowCount = authorityDictBo.update(vo);
		SpecificResult<Integer> result = new SpecificResult<Integer>(BaseResult.SUCCESS);
		result.setResultData(updateRowCount);

		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<AuthorityDictVo>> list(AuthorityDictSelectParam param) {
		SpecificPageInfoResult<AuthorityDictVo> specificPageInfoResult = authorityDictBo.list(param);

		SpecificResult<SpecificPageInfoResult<AuthorityDictVo>> result = new SpecificResult<>();
		result.setResultData(specificPageInfoResult);

		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "list_all")
	public SpecificResult<List<AuthorityDictVo>> listAll() {
		List<AuthorityDictVo> listAll = authorityDictBo.listAll();
		SpecificResult<List<AuthorityDictVo>> listAllResult = new SpecificResult<>(BaseResult.SUCCESS);
		listAllResult.setResultData(listAll);

		return listAllResult;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{dictId}")
	public SpecificResult<Integer> delete(@PathVariable("dictId") String dictId) {
		int deleteRowCount = authorityDictBo.delete(dictId);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(deleteRowCount);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value = "/{userId}")
	public SpecificResult<List<AuthorityDictVo>> listByUserId(AuthorityDictUserParam param) {
		List<AuthorityDictVo> authorityDictVos = authorityDictBo.listByUserId(param);
		SpecificResult<List<AuthorityDictVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(authorityDictVos);

		return result;
	}


	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value = "/role_id")
	public SpecificResult<List<AuthorityDictVo>> listByRoleId(@RequestParam String roleId) {
		List<AuthorityDictVo> authorityDictVos = authorityDictBo.listByRoleId(roleId);
		SpecificResult<List<AuthorityDictVo>> baseResult = new SpecificResult<>(BaseResult.SUCCESS);
		baseResult.setResultData(authorityDictVos);
		return baseResult;
	}
}
