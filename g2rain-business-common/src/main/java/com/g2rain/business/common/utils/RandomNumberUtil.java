package com.g2rain.business.common.utils;

public class RandomNumberUtil {

	/**
	 * 产生6位随机数字
	 * 
	 * @return
	 */
	public static String generateSixNumber() {
		return CommonStringUtil.formatSixChar(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));
	}

	public static void main(String[] args) {
		System.out.println(generateSixNumber());
	}
}
