package com.g2rain.business.core.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.annotations.Login;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.OrganBo;
import com.g2rain.business.core.po.OrganSelectParam;
import com.g2rain.business.core.vo.OrganVo;

@Controller
@RequestMapping("organ")
public class OrganController {

	@Autowired
	private OrganBo organBo;

	@Login(require = false)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, path = "/{organId}")
	public BaseResult list(@PathVariable String organId) {
		OrganVo organ = organBo.get(organId);
		SpecificResult<OrganVo> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(organ);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	public SpecificResult<Integer> update(@RequestBody OrganVo organ) {
		int updateRowCount = organBo.update(organ);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(updateRowCount);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/{organId}/status/{status}")
	public SpecificResult<Integer> updateStatus(@PathVariable("organId") String organId,
			@PathVariable("status") String status) {
		int updateRowCount = organBo.updateStatus(organId, status);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(updateRowCount);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{organId}")
	public SpecificResult<Integer> delete(@PathVariable("organId") String organId) {
		int deleteRowCount = organBo.delete(organId);
		SpecificResult<Integer> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(deleteRowCount);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SpecificResult<SpecificPageInfoResult<OrganVo>> list(OrganSelectParam param) {
		SpecificPageInfoResult<OrganVo> specificPageInfoResult = organBo.list(param);
		SpecificResult<SpecificPageInfoResult<OrganVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(specificPageInfoResult);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public SpecificResult<String> add(@RequestBody @Valid OrganVo organ) {
		String organId = organBo.add(organ);
		SpecificResult<String> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(organId);
		return result;
	}
}