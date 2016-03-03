package com.yunsoo.common.web.support;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by:   Lijian
 * Created on:   2016-03-02
 * Descriptions:
 */
public class ResourceInputStreamHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return ResourceInputStream.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            throw new NotFoundException();
        }
        ResourceInputStream resourceInputStream = (ResourceInputStream) returnValue;

//        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//        ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);

    }

}
