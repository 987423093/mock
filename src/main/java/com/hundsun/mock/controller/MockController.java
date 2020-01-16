package com.hundsun.mock.controller;

import com.google.gson.Gson;
import com.hundsun.mock.enums.MockExceptionEnum;
import com.hundsun.mock.request.MockBaseRequestVo;
import com.hundsun.mock.response.MockBaseResponseVo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: zhoutao29203
 * @date: 2020/1/15 18:32
 * @Copyright: 2020 Hundsun All rights reserved.
 */
@RestController
public class MockController {

    private static final Logger logger = LoggerFactory.getLogger(MockController.class);

    @Value("${mock.sendRequest}")
    private String url;

    private static Map<String, String> methodMap = new HashMap<>(16);

    static {

        //todo 事先定义好映射关系
        methodMap.put("g/hsxone.omc/v", "com.hundsun.jres.bizframe.cloud.api.IUserService");
    }

    /**
     * get请求
     * @param group
     * @param name
     * @param version
     * @param method
     * @param request
     * @return
     */
    @GetMapping(value = "{group}/{name}/{version}/{method}")
    public String getMock(@PathVariable String group, @PathVariable String name, @PathVariable String version,
                          @PathVariable String method, HttpServletRequest request){

        return this.dealMock(group, name, version, method, request);
    }

    /**
     * post请求
     * @param group
     * @param name
     * @param version
     * @param method
     * @param request
     * @return
     */
    @PostMapping(value = "{group}/{name}/{version}/{method}")
    public String postMock(@PathVariable String group, @PathVariable String name, @PathVariable String version,
                       @PathVariable String method, HttpServletRequest request) {

        return this.dealMock(group, name, version, method, request);
    }

    /**
     * 请求调用
     * @param group
     * @param name
     * @param version
     * @param method
     * @param request
     * @return
     */
    private String dealMock(String group, String name, String version, String method, HttpServletRequest request){

        MockBaseRequestVo mockBaseRequestVo = new MockBaseRequestVo();
        mockBaseRequestVo.setServiceName(this.getServiceName(group, name, version));
        mockBaseRequestVo.setFuncName(method);
        mockBaseRequestVo.setParam(this.getParams(request));
        //http
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig.DEFAULT);
        Gson gson = new Gson();
        String mockBaseRequestVoStr = gson.toJson(mockBaseRequestVo);
        httpPost.setEntity(new StringEntity(mockBaseRequestVoStr,
                ContentType.create("application/json", "utf-8")));
        String result;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            logger.error("http请求调用mock失败");
            return MockExceptionEnum.SERVER_ERROR.getDesc();
        }

        if (result == null) {
            logger.error("mock没有响应");
            return MockExceptionEnum.SERVER_ERROR.getDesc();
        }
        MockBaseResponseVo mockBaseResponseVo = gson.fromJson(result, MockBaseResponseVo.class);
        if (MockExceptionEnum.MOCK_INTERFACE_NOT_EXIST.getCode().equals(mockBaseResponseVo.getCode())) {
            logger.error("mock接口不存在");
            return MockExceptionEnum.MOCK_ERROR.getDesc();
        }
        if (MockExceptionEnum.MOCK_SUCCESS.getCode().equals(mockBaseResponseVo.getCode())) {
            return mockBaseResponseVo.getRsp();
        }
        logger.error("mock调用失败");
        return MockExceptionEnum.SERVER_ERROR.getDesc();
    }

    /**
     * 请求得到参数
     * @param request
     * @return
     */
    private String getParams(HttpServletRequest request){

        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 得到映射出来的服务名
     * @param group
     * @param name
     * @param version
     * @return
     */
    private String getServiceName(String group, String name, String version){

        String serviceName = methodMap.get(group + "/" + name + "/" + version);
        if (StringUtils.isEmpty(serviceName)){
            logger.error("找不到对应的服务");
            return MockExceptionEnum.PARAM_NOT_EMPTY_ERROR.getDesc();
        }
        return serviceName;
    }
}
