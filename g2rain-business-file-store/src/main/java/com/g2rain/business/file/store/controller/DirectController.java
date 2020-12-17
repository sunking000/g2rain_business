package com.g2rain.business.file.store.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.file.store.bo.FileObjectBo;
import com.g2rain.business.file.store.vo.FileObjectVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DirectController {

	@Autowired
	private FileObjectBo fileObjectBo;

	@RequestMapping("/direct/callback/aliyun")
	@ResponseBody
	public SpecificResult<FileObjectVo> aliyunCallback(@RequestParam Map<String, String> callbackParam) {
		log.info("/direct/callback/aliyun param:{}", JSONObject.toJSONString(callbackParam));
		FileObjectVo fileObjectVo = fileObjectBo.callback(callbackParam.get("fileObjectId"));
		SpecificResult<FileObjectVo> result = new SpecificResult<FileObjectVo>(BaseResult.SUCCESS);
		if (fileObjectVo != null) {
			result.setResultData(fileObjectVo);
		}
		
		return result;
	}
}
