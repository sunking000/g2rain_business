package com.g2rain.business.file.store.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.file.store.bo.FileBo;
import com.g2rain.business.file.store.vo.FileObjectVo;

@RequestMapping("file")
@Controller
public class FileController {

	@Autowired
	private FileBo fileBo;

	@ResponseBody
	@RequestMapping(value= "/upload", method = RequestMethod.POST)
	public SpecificResult<List<FileObjectVo>> fileUpload(HttpServletRequest request,
			@RequestParam MultipartFile[] files) throws IOException {
		List<FileObjectVo> fileObjectVos = fileBo.saveFiles(request, files);

		SpecificResult<List<FileObjectVo>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(fileObjectVos);

		return result;
    }
}
