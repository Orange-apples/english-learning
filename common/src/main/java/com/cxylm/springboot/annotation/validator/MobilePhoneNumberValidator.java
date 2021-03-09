package com.cxylm.springboot.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MobilePhoneNumberValidator implements ConstraintValidator<MobilePhone, String> {
    private static final Pattern regex = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[0-9])\\d{8}$");
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return regex.matcher(s).matches();
    }
}
