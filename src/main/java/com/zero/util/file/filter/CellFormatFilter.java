package com.zero.util.file.filter;

/**
 * @author yezhaoxing
 * @since 2018/06/14
 */
public class CellFormatFilter {

    public String filterSuccess(Object val) {
        return (Boolean) val ? "成功" : "失败";
    }
}
