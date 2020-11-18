package com.g2rain.business.file.store.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.g2rain.business.common.annotations.Login;
import com.g2rain.business.file.store.bo.ReadFileBo;
import com.g2rain.business.file.store.bo.domain.DownloadFileDomain;
import com.g2rain.business.file.store.enums.DownloadFileDomainActionEnum;

@RequestMapping("rf")
@Controller
public class ReadFileController {

	@Autowired
	private ReadFileBo readFileBo;

	@Login(require = false)
	@RequestMapping(method = RequestMethod.GET, value = "/{fileObjectId}")
	public void readFile(@PathVariable("fileObjectId") String fileObjectId, HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		DownloadFileDomain downloadFileDomain = readFileBo.readFile(fileObjectId);
		if (DownloadFileDomainActionEnum.REDIRECT == downloadFileDomain.getAction()) {
			response.sendRedirect(downloadFileDomain.getOssUrl());
		} else {
			File file = new File(downloadFileDomain.getFileUrl());
			byte[] byteArray = FileUtils.readFileToByteArray(file);

			response.setContentType(downloadFileDomain.getContentType());
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + downloadFileDomain.getFileName() + "\"");
			response.addHeader("Content-Length", byteArray.length + "");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(byteArray);
			outputStream.flush();
			outputStream.close();
		}
	}
}