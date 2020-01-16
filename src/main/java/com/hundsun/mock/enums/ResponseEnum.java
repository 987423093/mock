package com.hundsun.mock.enums;

/**
 * @Description:
 * @author: zhoutao29203
 * @date: 2020/1/16 14:41
 * @Copyright: 2020 Hundsun All rights reserved.
 */
public enum ResponseEnum {

    SUCCESS(200, "成功"),
    METHOD_NOT_FIND_ERROR(500, "没有找到对应的方法"),
    SERVICE_NAME_MAPPING_ERROR(500, "接口映射未匹配上"),
    HTTP_ERROR(500, "http请求调用mock失败"),
    MOCK_RESPONSE_ERROR(500, "mock没有响应"),
    MOCK_INTERFACE_ERROR(500, "mock接口不存在"),
    MOCK_OTHER_ERROR(500, "mock调用失败");

    private Integer code;

    private String desc;

    ResponseEnum(Integer code, String desc){

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
}
