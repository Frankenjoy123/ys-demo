package com.yunsoo.api.annotation;

import java.lang.annotation.*;

/**
 * Created by yan on 6/28/2016.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OperationLog {
    public String operation() default "";
}
