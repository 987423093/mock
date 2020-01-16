package com.hundsun.mock.config;

import com.hundsun.mock.enums.ResponseEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @author: zhoutao29203
 * @date: 2020/1/16 13:30
 * @Copyright: 2020 Hundsun All rights reserved.
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
                try {
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                    String errorMessage = "{ \"message\" : \"" + ResponseEnum.METHOD_NOT_FIND_ERROR.getDesc() + "\"}";
                    httpServletResponse.setStatus(ResponseEnum.METHOD_NOT_FIND_ERROR.getCode());
                    httpServletResponse.getWriter().write(errorMessage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new ModelAndView();
            }
        });
    }
}
