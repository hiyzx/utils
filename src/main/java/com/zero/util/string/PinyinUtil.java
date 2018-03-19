package com.zero.util.string;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

    /**
     * @Description: 将汉字转换为拼音
     */
    public static String converterToSpell(String chines) {
        if (chines == null || chines.trim().length() <= 0) {
            return "";
        }
        try {
            String pinyinName = "";
            char[] nameChar = chines.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < nameChar.length; i++) {
                if (nameChar[i] > 128) {
                    try {
                        pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    pinyinName += nameChar[i];
                }
            }
            return pinyinName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
