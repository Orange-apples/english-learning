package com.cxylm.springboot.config;

import com.cxylm.springboot.config.interceptor.ApiInterceptor;
import com.cxylm.springboot.config.interceptor.MerchantApiInterceptor;
import com.cxylm.springboot.factory.EnumConvertFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    private final ApiInterceptor apiInterceptor;
    private final MerchantApiInterceptor merchantApiInterceptor;
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

    public WebMVCConfig(ApiInterceptor apiInterceptor,
                        MerchantApiInterceptor merchantApiInterceptor) {
        this.apiInterceptor = apiInterceptor;
        this.merchantApiInterceptor = merchantApiInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/notify/**")
                .excludePathPatterns("/api/test/**")
                .excludePathPatterns("/api/category/**")
                .excludePathPatterns("/api/manager/**")
        ;

        registry.addInterceptor(merchantApiInterceptor)
                .addPathPatterns("/api/merchant/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
