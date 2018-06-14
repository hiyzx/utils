package com.zero.util.json;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yezhaoxing
 * @since 2018/6/14
 * @description jackson工具类
 */
public class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper;

    public static <T> T readValue(String jsonStr, Class<T> valueType) {
        T rtn = null;
        if (!StringUtils.isEmpty(jsonStr)) {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            try {
                rtn = objectMapper.readValue(jsonStr, valueType);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return rtn;
    }

    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
        T rtn = null;
        if (!StringUtils.isEmpty(jsonStr)) {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            try {
                rtn = objectMapper.readValue(jsonStr, valueTypeRef);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return rtn;
    }

    public static String toJSon(Object object) {
        String rtn = null;
        if (object != null) {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            try {
                rtn = objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return rtn;
    }
}
