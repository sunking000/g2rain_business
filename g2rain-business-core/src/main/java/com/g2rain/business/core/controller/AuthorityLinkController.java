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
import com.g2rain.business.core.bo.AuthorityLinkBo;
import com.g2rain.business.core.po.AuthorityLinkSelectParam;
import com.g2rain.business.core.vo.AuthorityLinkVo;

@RequestMapping("authority_link")
@Controller
public class AuthorityLinkController {

	@Autowired
	private AuthorityLinkBo authorityLinkBo;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<AuthorityLinkVo> add(@RequestBody AuthorityLinkVo authorityLink) {
		AuthorityLinkVo vo = authorityLinkBo.add(authorityLink);

		SpecificResult<AuthorityLinkVo> result = new SpecificResult<AuthorityLinkVo>(BaseResult.SUCCESS);
		result.setResultData(vo);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public BaseResult list(AuthorityLinkSelectParam param) {
		SpecificPageInfoResult<AuthorityLinkVo> specificPageInfoResult = authorityLinkBo.list(param);

		SpecificResult<SpecificPageInfoResult<AuthorityLinkVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(specificPageInfoResult);

		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value = "update")
	public BaseResult update(@RequestBody AuthorityLinkVo authorityLink) {
		int updateRowCount = authorityLinkBo.update(authorityLink);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(updateRowCount);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{linkId}")
	public SpecificResult<Integer> listByUserId(@PathVariable("linkId") String linkId) {
		int deleteRowCount = authorityLinkBo.deleteLink(linkId);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(deleteRowCount);

		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value = "/{userId}")
	public SpecificResult<List<AuthorityLinkVo>> listByUserId(@PathVariable("userId") String userId,
			@RequestParam String type) {
		List<AuthorityLinkVo> authorityLinkVos = authorityLinkBo.listByUserId(userId, type);
		SpecificResult<List<AuthorityLinkVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(authorityLinkVos);

		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value = "/role_id")
	public SpecificResult<List<AuthorityLinkVo>> listByRoleId(@RequestParam String roleId,
			@RequestParam(required = false) String linkType) {
		List<AuthorityLinkVo> authorityLinkVos = authorityLinkBo.listByRoleId(roleId, linkType);
		SpecificResult<List<AuthorityLinkVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(authorityLinkVos);

		return result;
	}
}
