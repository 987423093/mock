package com.hundsun.mock.response;

import java.io.Serializable;

/**
 * @Description:
 * @author: zhoutao29203
 * @date: 2020/1/15 16:45
 * @Copyright: 2020 Hundsun All rights reserved.
 */
public class MockBaseResponseVo implements Serializable {

    /**
     * mock返回code
     */
    private Integer code;

    /**
     * mock请求
     */
    private String req;

    /**
     * mock返回
     */
    private String rsp;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getRsp() {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }
}
