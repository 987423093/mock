package com.hundsun.mock.controller;

import com.google.gson.Gson;
import com.hundsun.mock.enums.MockExceptionEnum;
import com.hundsun.mock.enums.ResponseEnum;
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
import javax.servlet.http.HttpServletResponse;
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
    public void getMock(@PathVariable String group, @PathVariable String name, @PathVariable String version,
                          @PathVariable String method, HttpServletRequest request, HttpServletResponse response){

        this.dealMock(group, name, version, method, request, response);
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
    public void postMock(@PathVariable String group, @PathVariable String name, @PathVariable String version,
                       @PathVariable String method, HttpServletRequest request, HttpServletResponse response) {

        this.dealMock(group, name, version, method, request, response);
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
    private void dealMock(String group, String name, String version, String method, HttpServletRequest request, HttpServletResponse response){

        MockBaseRequestVo mockBaseRequestVo = new MockBaseRequestVo();
        String serviceName = methodMap.get(group + "/" + name + "/" + version);
        if (StringUtils.isEmpty(serviceName)){
            logger.error("接口映射未匹配上");
            this.setResponse(response, ResponseEnum.SERVICE_NAME_MAPPING_ERROR);
            return;
        }
        mockBaseRequestVo.setServiceName(serviceName);
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
            this.setResponse(response, ResponseEnum.HTTP_ERROR);
            return;
        }
        if (result == null) {
            logger.error("mock没有响应");
            this.setResponse(response, ResponseEnum.MOCK_RESPONSE_ERROR);
        }else {
            MockBaseResponseVo mockBaseResponseVo = gson.fromJson(result, MockBaseResponseVo.class);
            if (MockExceptionEnum.MOCK_INTERFACE_NOT_EXIST.getCode().equals(mockBaseResponseVo.getCode())) {
                logger.error("mock接口不存在");
                this.setResponse(response, ResponseEnum.MOCK_INTERFACE_ERROR);
            }else if (MockExceptionEnum.MOCK_SUCCESS.getCode().equals(mockBaseResponseVo.getCode())) {
                this.setResponse(response, ResponseEnum.SUCCESS.getCode(), mockBaseResponseVo.getRsp());
            }else{
                logger.error("mock调用失败");
                this.setResponse(response, ResponseEnum.MOCK_OTHER_ERROR);
            }
        }
    }

    /**
     * 统一设置返回值 错误信息包装成json
     * @param response
     * @param responseEnum
     */
    private void setResponse(HttpServletResponse response, ResponseEnum responseEnum){

        String errorMessage = "{ \"message\" : \"" + responseEnum.getDesc() + "\"}";
        this.setResponse(response, responseEnum.getCode(), errorMessage);
    }

    /**
     * 统一设置返回值
     * @param response
     * @param code
     * @param message
     */
    private void setResponse(HttpServletResponse response, Integer code, String message){

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(code);
        try {
            response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
