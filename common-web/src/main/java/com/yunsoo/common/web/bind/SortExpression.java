package com.yunsoo.common.web.bind;

import java.lang.annotation.*;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/25
 * Descriptions:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SortExpression {

    String value() default "sort";

    boolean required() default true;
}
