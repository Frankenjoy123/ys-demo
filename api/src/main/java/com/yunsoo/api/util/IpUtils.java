package com.yunsoo.api.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2016-02-22
 * Descriptions:
 */
public final class IpUtils {

    public static String getIpFromRequest(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-client-ip");
        if (validate(ipAddress)) {
            return ipAddress;
        }

        ipAddress = request.getHeader("x-forwarded-for");
        ipAddress = ipAddress == null ? null : ipAddress.split(",")[0].split(":")[0];
        if (validate(ipAddress)) {
            return ipAddress;
        }

        ipAddress = request.getHeader("x-real-ip");
        if (validate(ipAddress)) {
            return ipAddress;
        }

        ipAddress = request.getRemoteAddr();
        if (validate(ipAddress)) {
            return ipAddress;
        }

        return null;
    }

    public static boolean validate(String ip) {
        return ip != null && ip.length() >= 7 && ip.length() <= 15;
    }

}
