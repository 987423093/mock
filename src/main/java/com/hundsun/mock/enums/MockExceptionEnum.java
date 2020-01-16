package com.hundsun.mock.enums;

/**
 * @Description:
 * @author: zhoutao29203
 * @date: 2020/1/16 9:03
 * @Copyright: 2020 Hundsun All rights reserved.
 */
public enum MockExceptionEnum {

    MOCK_INTERFACE_NOT_EXIST(11, "mock接口不存在"),
    MOCK_SUCCESS(0, "mock调用成功");

    private Integer code;

    private String desc;

    MockExceptionEnum(Integer code, String desc){

       this.code = code;
       this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
