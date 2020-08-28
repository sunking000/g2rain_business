package com.g2rain.business.common.utils;

import java.util.Random;

/**
 * @author Administrator
 * @version V1.0
 * @Description:
 * @Title: RandomUtil
 * @Package com.sunhaojie.pos.common.utils
 * @date 2018\11\18 0018 2018
 */
public class RandomUtil {

    private static String SOURCES_NUM = "0123456789";

    public static String getNum(int length) {
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < length; j++) {
            flag.append(SOURCES_NUM.charAt(rand.nextInt(9)) + "");
        }
        return flag.toString();
    }

    public static void main(String[] args) {
        System.out.println(getNum(18));
    }
}
