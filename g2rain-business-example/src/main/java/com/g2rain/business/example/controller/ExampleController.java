package com.g2rain.business.example.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.annotations.AutoFill;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.example.bo.ExampleBo;
import com.g2rain.business.example.po.param.ExampleSelectParam;
import com.g2rain.business.example.vo.ExampleVo;
import com.g2rain.business.example.vo.param.AddOrUpdateExampleParam;

@RequestMapping("example")
@Controller
public class ExampleController {

	@Autowired
	private ExampleBo exampleBo;

	@AutoFill(storeIdRequire = true)
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public SpecificResult<Integer> addOrUpdate(@RequestBody @Valid AddOrUpdateExampleParam param) {
		int editRowCount = exampleBo.addOrUpdate(param);

		SpecificResult<Integer> result = new SpecificResult<>(SpecificResult.SUCCESS);
		result.setResultData(editRowCount);

		return result;
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseBody
	public SpecificResult<Integer> delete(String exampleIds) {
		int deleteRowCount = exampleBo.delete(exampleIds);

		SpecificResult<Integer> result = new SpecificResult<>(SpecificResult.SUCCESS);
		result.setResultData(deleteRowCount);

		return result;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public SpecificResult<SpecificPageInfoResult<ExampleVo>> list(ExampleSelectParam param) {
		SpecificPageInfoResult<ExampleVo> pageInfoResult = exampleBo.list(param);

		SpecificResult<SpecificPageInfoResult<ExampleVo>> result = new SpecificResult<>(SpecificResult.SUCCESS);
		result.setResultData(pageInfoResult);

		return result;
	}
}