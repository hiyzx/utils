package com.zero.util.security;

import com.zero.util.rsa.Base64;

public class CipherTextUtil {
    private static final String SEPARATOR = "-";

    public static String encode(int text1, String text2) {
        return encode(new String[] { text2, String.valueOf(text1) });
    }

    public static int decodeText1(String cipherText) {
        cipherText = decode(cipherText);
        return Integer.valueOf(cipherText.substring(cipherText.lastIndexOf(SEPARATOR) + 1));
    }

    public static String decodeText2(String cipherText) {
        cipherText = decode(cipherText);
        return cipherText.substring(0, cipherText.lastIndexOf(SEPARATOR));
    }

    private static String decode(String cipherText) {
        StringBuilder sb = new StringBuilder(cipherText.length() + 3).append(cipherText);
        for (int j = 0; j < sb.length() % 4; j++) {
            sb.append("=");
        }
        cipherText = sb.toString();
        return new String(Base64.decode(cipherText));
    }

    private static String encode(String[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            sb.append(tokens[i]);
            if (i < tokens.length - 1) {
                sb.append(SEPARATOR);
            }
        }
        String value = sb.toString();
        sb = new StringBuilder(Base64.encode(value.getBytes()));
        while (sb.charAt(sb.length() - 1) == '=') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
