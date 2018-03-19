package com.zero.util.http;

/**
 * 健康检查的对象
 * 
 * @author yezhaoxing
 * @date 2017/5/18
 */
public class HealthCheckVo {

    private String serviceName;

    private boolean isNormal;

    private String costTime;

    private String remark;

    public HealthCheckVo() {
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isNormal() {
        return this.isNormal;
    }

    public void setNormal(boolean isNormal) {
        this.isNormal = isNormal;
    }

    public String getCostTime() {
        return this.costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
