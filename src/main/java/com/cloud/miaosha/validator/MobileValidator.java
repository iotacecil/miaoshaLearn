package com.cloud.miaosha.validator;

import com.cloud.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile,String> {
    //成员变量，接收注解定义
    private boolean required = false;
    @Override
    public void initialize(Mobile constraintAnnotation) {
    required = constraintAnnotation.required();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required){
            //如果是必须的，判断是否合法
            return ValidatorUtil.isMobile(value);
        }else//如果不是必须的
            if(StringUtils.isEmpty(value)){
            return true;

        }else{
            return ValidatorUtil.isMobile(value);
            }
    }
}
