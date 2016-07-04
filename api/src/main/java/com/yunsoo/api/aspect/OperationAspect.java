package com.yunsoo.api.aspect;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by yan on 6/30/2016.
 */
@Aspect
public class OperationAspect {

    private Log log = LogFactory.getLog(this.getClass());

    @After("execution(* com.yunsoo.api.controller.*.*(..)) && @annotation(operation)")
    public void recordOperation(ProceedingJoinPoint point, OperationLog operation){
        log.info("in the operation aspect, action: " + operation.operation());
    }

}
