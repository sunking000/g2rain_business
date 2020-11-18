package com.g2rain.business.file.store.vo;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.g2rain.business.common.domain.BaseVo;
import com.g2rain.business.file.store.po.FileObjectPo;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件对象
 * 
 * @author sunhaojie
 *
 */
@Getter
@Setter
@JsonIgnoreProperties("serverContextPath")
public class FileObjectVo extends BaseVo {
	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件的md5
	 */
	private String md5;
	/**
	 * 文件类型，普通文件,图片,视频
	 */
	private String fileType;
	/**
	 * 文件存储类型，本地存储,阿里云OSS
	 */
	private String storeType;
	/**
	 * 存储相对路径
	 */
	private String storePath;
	/**
	 * 描述
	 */
	private String description;
	private String serverContextPath;

	public String getFileUrl() {
		return this.serverContextPath + this.fileId;
	}

	public FileObjectVo() {
		super();
	}

	public FileObjectVo(FileObjectPo po) {
		super(po);
		BeanUtils.copyProperties(po, this);
	}
}
