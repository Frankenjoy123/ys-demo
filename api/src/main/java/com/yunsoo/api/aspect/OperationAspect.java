package com.yunsoo.api.aspect;


import com.yunsoo.api.Constants;
import com.yunsoo.api.cache.OperationCache;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.IpUtils;
import com.yunsoo.api.util.RequestUtils;
import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        try {
            log.info("in the operation aspect, action: " + joinPoint.getSignature().getName());
            Object[] args = joinPoint.getArgs();
            Method method = getMethod(joinPoint);

            OperationLogObject object = new OperationLogObject();
            object.setCreatedDateTime(DateTime.now());
            if (method != null)
                object.setOperation(parseKey(operationLog.operation(), method, args));
            else
                object.setOperation(operationLog.operation());
            object.setLevel(operationLog.level());

            try {
                object.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
            } catch (UnauthorizedException ex) {  //not login
                object.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);

            }


            HttpServletRequest request = RequestUtils.getCurrentHttpServletRequest();
            if (request != null) {
                object.setUserAgent(request.getHeader(Constants.HttpHeaderName.USER_AGENT));
                String appId = request.getHeader(Constants.HttpHeaderName.APP_ID);
                object.setCreatedAppId(StringUtils.hasText(appId) ? appId : Constants.Ids.ENTERPRISE_APP_ID);
                object.setIp(IpUtils.getIpFromRequest(request));
                OperationCache.put(object);
            }
        } catch (Exception e) {
            log.error("Operation aspect error:" + joinPoint.getSignature().getName(), e);
        }

    }

    private Method getMethod(JoinPoint pjp) {
        //获取参数的类型
        Object[] args = pjp.getArgs();
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {

            if (args[i] == null)
                argTypes[i] = String.class;
            else if (args[i].getClass().equals(ArrayList.class))
                argTypes[i] = List.class;
            else
                argTypes[i] = args[i].getClass();

        }
        Method method = null;
        try {
            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            log.error("Operation aspect, no such method found", e);

        } catch (SecurityException ex) {
            log.error("Operation aspect, security issue found", ex);
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


}
