package com.g2rain.business.file.store.po.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OssAddFileObjectParam {
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件类型，普通文件,图片,视频
	 */
	private String fileType;
	/**
	 * 描述
	 */
	private String description;

	public OssAddFileObjectParam() {
		super();
	}
}
