package com.g2rain.business.common.result;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificPageInfoResult<T> {
	private List<T> objects;
	private PageInfoResult pageInfoResult;

	public SpecificPageInfoResult() {
		pageInfoResult = new PageInfoResult();
	}

	public SpecificPageInfoResult(int pageNum, int pageSize, long total, int pages) {
		pageInfoResult = new PageInfoResult(pageNum, pageSize, total, pages);
	}

	public SpecificPageInfoResult(int pageNum, int pageSize, long total, int pages, List<T> objects) {
		pageInfoResult = new PageInfoResult(pageNum, pageSize, total, pages);
		this.objects = objects;
	}

	public static void main(String[] args) {
		System.out.println(JSONObject.toJSONString(new SpecificPageInfoResult<Object>(1, 10, 20, 2)));
	}
}