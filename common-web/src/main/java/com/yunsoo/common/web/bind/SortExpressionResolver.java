package com.yunsoo.common.web.bind;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/25
 * Descriptions:
 */
public class SortExpressionResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SortExpression.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory)
            throws Exception {


        return null;
    }
//
//    @Override
//    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
//
//        return null;
//    }


}
