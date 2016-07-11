package com.yunsoo.api.aspect;


import com.yunsoo.api.Constants;
import com.yunsoo.api.cache.OperationCache;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.IpUtils;
import com.yunsoo.common.data.object.OperationLogObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by yan on 6/30/2016.
 */
@Component
@Aspect
public class OperationAspect {


    private Log log = LogFactory.getLog(this.getClass());

    public OperationAspect() {
        log.info("init OperationAspect");
    }

    @After("execution(* com.yunsoo.api.controller.*.*(..)) && @annotation(operationLog)")
    public void recordOperation(JoinPoint joinPoint, OperationLog operationLog) {

        log.info("in the operation aspect, action: " + operationLog.operation());
        Object[] args = joinPoint.getArgs();
        Method method = getMethod(joinPoint);

        OperationLogObject object = new OperationLogObject();
        object.setCreatedDateTime(DateTime.now());
        object.setOperation(operationLog.operation() + " " + parseKey(operationLog.target(), method, args));
        object.setLevel(operationLog.level());
        object.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());

        HttpServletRequest request = getCurrentHttpServletRequest();
        if (request != null) {
            object.setUserAgent(request.getHeader(Constants.HttpHeaderName.USER_AGENT));
            object.setCreatedAppId(request.getHeader(Constants.HttpHeaderName.APP_ID));
            object.setIp(IpUtils.getIpFromRequest(request));
            OperationCache.put(object);
        }

    }

    private Method getMethod(JoinPoint pjp) {
        //获取参数的类型
        Object[] args = pjp.getArgs();
        Class[] argTypes = new Class[1];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return method;

    }

    //get the value of Spel key
    private String parseKey(String key, Method method, Object[] args) {

        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    private HttpServletRequest getCurrentHttpServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getRequest();
    }

}
