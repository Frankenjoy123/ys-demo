package com.yunsoo.api.security.annotation;

import com.yunsoo.api.security.permission.Action;

import java.lang.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    String resource() default "";

    Action action() default Action.NONE;

}
