package com.g2rain.business.core.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.OrganMembershipBo;
import com.g2rain.business.core.po.param.OrganMembershipSelectParam;
import com.g2rain.business.core.vo.AddOrganMembershipParam;
import com.g2rain.business.core.vo.OrganMembershipVo;

@RequestMapping("organ_membership")
@Controller
public class OrganMembershipController {
	@Autowired
	private OrganMembershipBo organMembershipBo;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<Integer> add(@RequestBody AddOrganMembershipParam param) {
		int addRowCount = organMembershipBo.add(param);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(addRowCount);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	public SpecificResult<Integer> delete(@RequestParam("parentOrganId") String parentOrganId,
			@RequestParam("childOrganId") String childOrganId) {
		int deleteRowCount = organMembershipBo.delete(parentOrganId, childOrganId);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(deleteRowCount);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<OrganMembershipVo>> list(OrganMembershipSelectParam param) {
		SpecificPageInfoResult<OrganMembershipVo> specificPageInfoResult = organMembershipBo.list(param);
		SpecificResult<SpecificPageInfoResult<OrganMembershipVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(specificPageInfoResult);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, path = "/hold_store")
	public SpecificResult<Set<String>> getHoldStoreId(@RequestParam String organId) {
		Set<String> holdStoreIds = organMembershipBo.getHoldStoreIds(organId);
		SpecificResult<Set<String>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(holdStoreIds);
		return result;
	}
}