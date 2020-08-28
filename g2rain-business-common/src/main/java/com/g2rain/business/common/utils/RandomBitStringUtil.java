package com.g2rain.business.common.utils;

import java.util.UUID;

/**
 *
 * @ClassName RandomEightBitStringUtil
 * @Description 随机八位字符串
 *
 * @author sunhaojie@kingsoft.com
 * @date 2017年8月3日 下午7:10:25
 */
public class RandomBitStringUtil {
    public static String[] chars = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
        "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z"};

    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

	public static String generateMiddleUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 16; i++) {
			String str = uuid.substring(i * 2, i * 2 + 2);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();

	}

	public static String generateLongUuid() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		// for (int i = 0; i < 32; i++) {
		// String str = uuid.substring(i * 4, i * 4 + 4);
		// int x = Integer.parseInt(str, 16);
		// shortBuffer.append(chars[x % 0x3E]);
		// }
		return uuid;

	}

    /**
     * 获取多个随机码
     *
     * @Title generateShortUuid
     * @param count
     * @return String[]
     *
     * @author sunhaojie@kingsoft.com
     * @date 2017年8月3日 下午7:13:06
     */
    public static String[] generateShortUuid(int count) {
        if (count <= 0) {
            count = 0;
        }

        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = generateShortUuid();
        }

        return result;
    }

    public static void main(String[] args) {
        String shortUUID = RandomBitStringUtil.generateShortUuid();
        System.out.println(shortUUID);

		String middleUUID = RandomBitStringUtil.generateMiddleUuid();
		System.out.println(middleUUID);

		String longUUID = RandomBitStringUtil.generateLongUuid();
		System.out.println(longUUID);
    }
}
