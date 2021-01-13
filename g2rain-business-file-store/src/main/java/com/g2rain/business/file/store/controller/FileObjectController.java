package com.g2rain.business.file.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.file.store.bo.FileObjectBo;
import com.g2rain.business.file.store.po.param.FileObjectSelectParam;
import com.g2rain.business.file.store.vo.FileObjectVo;

@RequestMapping("file_object")
@Controller
public class FileObjectController {

	@Autowired
	private FileObjectBo fileObjectBo;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public SpecificResult<SpecificPageInfoResult<FileObjectVo>> list(FileObjectSelectParam param) {

		SpecificPageInfoResult<FileObjectVo> specificPageInfoResult = fileObjectBo.list(param);
		
		SpecificResult<SpecificPageInfoResult<FileObjectVo>> specificResult = new SpecificResult<>(BaseResult.SUCCESS);
		specificResult.setResultData(specificPageInfoResult);

		return specificResult;
	}
}
