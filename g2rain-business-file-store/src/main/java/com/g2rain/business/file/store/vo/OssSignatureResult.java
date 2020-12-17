package com.g2rain.business.file.store.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OssSignatureResult {
	private String accessId;
	private String encodedPolicy;
	private String postSignature;
	private String dir;
	private String host;
	private String expire;
	private String callback;

	public OssSignatureResult() {
		super();
	}
}
