package com.joe.rxbus.annotation;

import com.joe.rxbus.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description
 * Created by chenqiao on 2016/1/15.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscriber {
    String tag() default "default_tag";

    ThreadMode mode() default ThreadMode.MAIN;
}