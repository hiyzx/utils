package com.zero.util.http;

import lombok.Data;

/**
 * 健康检查的对象
 * 
 * @author yezhaoxing
 * @date 2017/5/18
 */
@Data
public class HealthCheckVo {

    private String serviceName;

    private boolean isNormal;

    private String costTime;

    private String remark;
}
