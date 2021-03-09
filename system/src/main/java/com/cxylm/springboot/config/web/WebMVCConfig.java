package com.cxylm.springboot.config.web;

import com.cxylm.springboot.config.ManagerApiInterceptor;
import com.cxylm.springboot.factory.EnumConvertFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    private final ManagerApiInterceptor managerApiInterceptor;

    @Autowired
    private EnumConvertFactory enumConvertFactory;

    /**
     * 传参转枚举
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConvertFactory);
    }

    public WebMVCConfig(ManagerApiInterceptor managerApiInterceptor) {
        this.managerApiInterceptor = managerApiInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(managerApiInterceptor)
                .addPathPatterns("/api/manager/**")
                .excludePathPatterns("/api/manager/user/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
