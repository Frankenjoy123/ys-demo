package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.config.AuthCacheConfig;
import com.yunsoo.auth.dto.HeartBeatPackage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by:   Lijian
 * Created on:   2016-07-25
 * Descriptions:
 */
@Service
public class HeartBeatService {

    private static final int OFFLINE_SECONDS = 600;

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Cached cached;

    public HeartBeatPackage getBeat(String deviceId) {
        HeartBeatPackage pkg = null;
        try {
            pkg = cached.getHeartBeatPackage(deviceId);
        } catch (Exception e) {
            log.error("getHeartBeatPackage failed", e);
        }
        if (pkg == null || pkg.getLastBeatDateTime() == null || pkg.getLastBeatDateTime().plusSeconds(OFFLINE_SECONDS).isBeforeNow()) {
            pkg = new HeartBeatPackage();
            pkg.setDeviceId(deviceId);
            pkg.setStatus(Constants.DeviceStatus.OFFLINE);
        }
        return pkg;
    }

    public HeartBeatPackage beat(String deviceId, String appId) {
        Assert.hasText(deviceId, "deviceId must not be null or empty");
        Assert.hasText(appId, "appId must not be null or empty");
        HeartBeatPackage pkg = new HeartBeatPackage();
        pkg.setDeviceId(deviceId);
        pkg.setAppId(appId);
        pkg.setStatus(Constants.DeviceStatus.ONLINE);
        return cached.putHeartBeatPackage(pkg);
    }


    @AuthCacheConfig
    @Service
    static class Cached {

        @Cacheable(key = "'heartbeat:device/' + #deviceId")
        public HeartBeatPackage getHeartBeatPackage(String deviceId) {
            return null;
        }

        @CachePut(key = "'heartbeat:device/' + #pkg.deviceId")
        public HeartBeatPackage putHeartBeatPackage(HeartBeatPackage pkg) {
            pkg.setLastBeatDateTime(DateTime.now());
            return pkg;
        }

    }

}
