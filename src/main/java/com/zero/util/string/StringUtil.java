package com.zero.util.string;

import com.zero.util.regexp.RegExpConstant;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zero.util.regexp.RegExpConstant.*;

/**
 * @author yezhaoxing
 * @date 2017/08/08
 */
public class StringUtil {

    /**
     * 去掉提交参数中的特殊字符
     */
    public static String replaceBlank(String source) {
        String dest = "";
        if (source != null) {
            Matcher m = Pattern.compile(RegExpConstant.BLANK_PATTERN).matcher(source);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String removeHtmlTag(String htmlStr) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(htmlStr)) {
            return null;
        }
        htmlStr = StringEscapeUtils.unescapeHtml4(htmlStr);
        Pattern p_w = Pattern.compile(REG_EX_W, Pattern.CASE_INSENSITIVE);
        Matcher m_w = p_w.matcher(htmlStr);
        htmlStr = m_w.replaceAll(""); // 过滤script标签

        Pattern p_script = Pattern.compile(REG_EX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(REG_EX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(REG_EX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(REG_EX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

        htmlStr = htmlStr.replaceAll(" ", ""); // 过滤
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String keyWordForSearch(String keyWord) {
        String rtn = null;
        if (StringUtils.isNotBlank(keyWord)) {
            String[] arr = keyWord.split("");
            StringBuilder sb = new StringBuilder(1024);
            sb.append("%");
            for (String s : arr) {
                sb.append(s).append("%");
            }
            rtn = sb.toString();
        }
        return rtn;
    }

    public static String filterEmoji(String source) {
        return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
    }
}