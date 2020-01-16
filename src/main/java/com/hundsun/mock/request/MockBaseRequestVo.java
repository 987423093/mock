package com.hundsun.mock.request;

import java.io.Serializable;

/**
 * @Description:
 * @author: zhoutao29203
 * @date: 2020/1/15 17:25
 * @Copyright: 2020 Hundsun All rights reserved.
 */
public class MockBaseRequestVo implements Serializable {

    /**
     * 包名+接口名
     */
    private String serviceName;

    /**
     * 方法名
     */
    private String funcName;

    /**
     * 字符串参数
     */
    private String param;

    /**
     * 头
     */
    private Object headers;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Object getHeaders() {
        return headers;
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }
}
