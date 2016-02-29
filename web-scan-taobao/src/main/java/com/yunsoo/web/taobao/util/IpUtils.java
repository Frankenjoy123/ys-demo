package com.yunsoo.web.taobao.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
public final class IpUtils {

    public static String getIpFromRequest(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.hasText(ipAddress)) {
            return ipAddress.split(",")[0].split(":")[0];
        } else {
            return request.getRemoteAddr();
        }
    }

}
