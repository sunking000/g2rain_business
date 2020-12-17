package com.g2rain.business.file.store.po;

import org.springframework.beans.BeanUtils;

import com.g2rain.business.common.domain.BasePo;
import com.g2rain.business.file.store.vo.FileObjectVo;

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
public class FileObjectPo extends BasePo {
	private String organId;
	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 文件名称
	 */
	private String fileName;
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
	/**
	 * 状态
	 */
	private String status;

	public FileObjectPo() {
		super();
	}

	public FileObjectPo(FileObjectVo vo) {
		super();
		BeanUtils.copyProperties(vo, this);
	}
}
