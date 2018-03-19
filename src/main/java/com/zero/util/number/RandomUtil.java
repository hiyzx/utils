package com.zero.util.number;

import java.util.Random;
import java.util.UUID;

/**
 * 
 * ClassName: RandomHelper
 * 
 * @Description: 随机数帮助类
 * @author angelo
 * @date 2016-2-23
 */
public class RandomUtil {

    /**
     * @Description: 获取随机数
     */
    public static String getSixRandCode(int num) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num; i++) {
            result.append(random.nextInt(9));
        }
        return result.toString();
    }

    /**
     * 获得去掉“-”符号的UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 去掉“字母”和“-”的UUID
     *
     * @param count
     *
     */
    public static String getUUID(int count) {
        String s = UUID.randomUUID().toString();
        return s.replaceAll("[a-z|-]", "").substring(0, count);
    }
}
