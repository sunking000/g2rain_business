package com.g2rain.business.common.result;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(value = "getPageInfoResult", allowGetters = true)
@Getter
@Setter
public class PageInfoResult implements Serializable {
	private static final long serialVersionUID = 7696052887117133868L;
	// 当前页
	private int pageNum;
	// 每页的数量
	private int pageSize;
	// 总记录数
	private long total;
	// 总页数
	private int pages;

	public PageInfoResult() {
	}

	public PageInfoResult(int pageNum, int pageSize, long total, int pages) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
		this.pages = pages;
	}
}
