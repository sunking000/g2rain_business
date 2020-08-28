package com.g2rain.business.core.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author Administrator
 * @Description
 * @date 2018/12/19
 */
@Configuration
public class ValidatorConfiguration {

    //设置校验模式 fail_fast：true  快速失败返回模式    false 普通模式
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }

}
