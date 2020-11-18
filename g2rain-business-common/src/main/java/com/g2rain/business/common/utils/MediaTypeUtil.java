package com.g2rain.business.common.utils;

import java.net.URLConnection;

import org.apache.commons.lang.StringUtils;

/**
 * http media type校验
 * 
 * @author sunhaojie
 *
 */
public class MediaTypeUtil {


	/**
	 * 是否为文件上传格式
	 * 
	 * @param contentType
	 * @return
	 */
	public static boolean isMultipartFormData(String contentType) {
		if (StringUtils.isEmpty(contentType)) {
			return false;
		}
		contentType = contentType.toLowerCase();
		return contentType.indexOf("multipart/form-data") >= 0 ? true : false;
	}

	/**
	 * 是否为字符形式
	 * 
	 * @return
	 */
	public static boolean isText(String contentType) {
		if (contentType == null) {
			return false;
		}
		String majorTypeString = null;
		String minorTypeString = null;
		String[] contentTypes = contentType.split("/");
		majorTypeString = contentTypes[0];
		minorTypeString = contentTypes[1];

		if (majorTypeString.equalsIgnoreCase("audio") || majorTypeString.equalsIgnoreCase("image")
				|| majorTypeString.equalsIgnoreCase("video")) {
			return false;
		}

		if (majorTypeString.equalsIgnoreCase("application")) {
			if (minorTypeString.equalsIgnoreCase("octet-stream")) {
				return false;
			}
		}


		return true;
	}

	public static String getContentType(String fileType) {
		String mimeType = URLConnection.guessContentTypeFromName(fileType);

		return mimeType;
	}

	public static void main(String[] args) {
		System.out.println(getContentType(".png"));
	}
}
