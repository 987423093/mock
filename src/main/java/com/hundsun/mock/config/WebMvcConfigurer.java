package com.hundsun.mock.config;

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

    private final static String  DEFAULT_RESPONSE = "{}";
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
                try {
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                    httpServletResponse.setStatus(200);
                    httpServletResponse.getWriter().write(DEFAULT_RESPONSE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new ModelAndView();
            }
        });
    }
}
