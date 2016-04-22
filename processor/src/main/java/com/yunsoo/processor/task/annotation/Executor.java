package com.yunsoo.processor.task.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Executor {
    String value() default "";
}
