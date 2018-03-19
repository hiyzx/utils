package com.zero.util.security;

import com.zero.util.rsa.Base64;

public class CiphertextUtil {
    private static final String SEPARATOR = "-";

    public static String encode(int teacherId, String uuid) {
        return encode(new String[] { uuid, String.valueOf(teacherId) }, SEPARATOR);
    }

    public static int decodeTeacherId(String ciphertext) {
        ciphertext = decode(ciphertext);
        return Integer.valueOf(ciphertext.substring(ciphertext.lastIndexOf(SEPARATOR) + 1));
    }

    public static String decodeUuid(String ciphertext) {
        ciphertext = decode(ciphertext);
        return ciphertext.substring(0, ciphertext.lastIndexOf(SEPARATOR));
    }

    static String decode(String ciphertext) {
        StringBuilder sb = new StringBuilder(ciphertext.length() + 3).append(ciphertext);
        for (int j = 0; j < sb.length() % 4; j++) {
            sb.append("=");
        }
        ciphertext = sb.toString();
        return new String(Base64.decode(ciphertext));
    }

    static String encode(String[] tokens, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            sb.append(tokens[i]);
            if (i < tokens.length - 1) {
                sb.append(separator);
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
