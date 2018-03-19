package com.zero.util.regexp;


public interface RegExpConstant {
    String IS_PHONE_NUMBER = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}"; // 手机号码正则

    String IS_HTTP_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"; // 验证网址Url

    String IS_EAMIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"; //验证邮箱

    String IS_IMEI = "^[0-9A-Za-z]{14,70}$"; // 验证设备号

    String IS_PASSWORD = "^[0-9a-zA-Z]{6,12}$"; // 验证密码必须是6-12位字符(数字和字母)

    String IS_NIKE_NAME = "^[a-zA-Z0-9_\u4e00-\u9fa5]+${6,12}$"; // 用户呢称6-12位正则(只含有汉字、数字、字母、下划线，下划线位置不限)

    String IS_BIRTHDAY = "\\d{4}-\\d{2}-\\d{2}"; // 验证用户的生日(yyyy-mm-dd)格式

    String IS_POSITIVE_INTEGER = "^\\+?[1-9][0-9]*$"; // 只能输入非零的正整数

    String IS_CHINESE = "^[\u4e00-\u9fa5]+$"; //判断是否为汉字

    String IS_ID_CARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$"; //判断身份证

    String IS_IP = "(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)"; //判断是否ip地址

    String BLANK_PATTERN = "\\s*|\t|\r|\n";

    String REG_EX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式

    String REG_EX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式

    String REG_EX_HTML = "<[^>]+>"; // 定义HTML标签的正则表达式

    String REG_EX_SPACE = "\\s*|\t|\r|\n";// 定义空格回车换行符

    String REG_EX_W = "<w[^>]*?>[\\s\\S]*?<\\/w[^>]*?>";// 定义所有w标签

}
