package com.cxylm.springboot.annotation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobilePhoneNumberValidator.class)
public @interface MobilePhone {
    String message() default "号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
