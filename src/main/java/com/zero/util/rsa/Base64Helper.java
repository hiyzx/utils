package com.zero.util.rsa;

import sun.misc.BASE64Decoder;

import java.io.IOException;

/**
 * RSA64编码或转码工具类
 */
public class Base64Helper {

    /**
     * 编码Byte[]转字符串
     */
    public static String encode(byte[] byteArray) {
        sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        return base64Encoder.encode(byteArray);
    }

    /**
     * 编码字符中转Byte[]
     */
    public static byte[] decode(String base64EncodedString) {
        BASE64Decoder base64Decoder = new sun.misc.BASE64Decoder();
        try {
            return base64Decoder.decodeBuffer(base64EncodedString);
        } catch (IOException e) {
            return null;
        }
    }

}