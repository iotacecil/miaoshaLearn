package com.cloud.miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//元注解meta-annotation
//注解的作用域列表
//CONSTRUCTOR构造方法 FIELD字段 LOCAL_VARIABLE局部变量 METHOD方法声明 PACKAGE 包声明 PARAMETER参数 TYPE类，接口
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
//生命周期SOURCE源码显示，编译丢弃 CLASS编译记录到class运行丢弃，RUNTIME运行存在可以通过反射读取
@Retention(RUNTIME)
//允许子注解继承
//@Inherited
//生成javadoc会包含注解信息
@Documented
@Constraint(validatedBy = {MobileValidator.class })
public @interface Mobile {
    //成员类型
    //只有一个成员变量的要叫value
    boolean required() default true;
    //默认信息
    String message() default "手机号码格式错误";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
