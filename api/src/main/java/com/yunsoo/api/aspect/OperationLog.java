package com.yunsoo.api.aspect;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.*;

/**
 * Created by yan on 6/28/2016.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OperationLog {
     String operation() default "";
     String level() default "";
}
