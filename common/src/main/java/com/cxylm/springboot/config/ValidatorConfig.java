package com.cxylm.springboot.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ValidatorConfig {
    // 配置方法参数校验
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        // 设置validator模式为快速失败返回
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    @Bean
    public Validator validator() {
        return Validation
                .byProvider(HibernateValidator.class)
                .configure()
                //快速返回模式，有一个验证失败立即返回错误信息
                .failFast(true)
                //.addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory()
                .getValidator();
    }

    // @Bean
    // public LocalValidatorFactoryBean getValidatorFactory() {
    //     LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    //     localValidatorFactoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
    //     return localValidatorFactoryBean;
    // }
}
