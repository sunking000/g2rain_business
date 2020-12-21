package com.g2rain.business.file.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.file.store.bo.OssBo;
import com.g2rain.business.file.store.po.param.OssAddFileObjectParam;
import com.g2rain.business.file.store.vo.OssSignatureResult;

@RequestMapping("oss")
@Controller
public class OssController {

	@Autowired
	private OssBo ossBo;

	@ResponseBody
	@RequestMapping("sign_4_upload")
	public SpecificResult<OssSignatureResult> signUploadObjectUrl(@RequestBody OssAddFileObjectParam param) {
		OssSignatureResult signUploadObjectUrl = ossBo.signUploadObjectUrl(param);
		SpecificResult<OssSignatureResult> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(signUploadObjectUrl);

		return result;
	}
}