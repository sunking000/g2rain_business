package com.g2rain.business.file.store.po.param;

import com.g2rain.business.common.domain.StoreBaseSelectParam;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileObjectSelectParam extends StoreBaseSelectParam {
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
	 * 名称模式
	 */
	private String namePattern;
	/**
	 * 状态
	 */
	private String status;

	public FileObjectSelectParam() {
		super();
	}

	public String getNamePattern() {
		return namePattern == null ? null : "%" + namePattern + "%";
	}
}
