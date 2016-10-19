package com.yunsoo.api.config;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.WeChatAPIDomain;
import com.yunsoo.api.thread.OperationThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by yan on 7/16/2016.
 */
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduleConfiguration {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    OperationThread thread;

    @Autowired
    WeChatAPIDomain weChatAPIDomain;

    @Value("${yunsoo.wechat.app_id}")
    private String weChatAppId;

    @Value("${yunsoo.wechat.app_secret}")
    private String weChatAppSecret;

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 300 * 1000)
    public void daemon() {
        try {
            thread.run();
        } catch (Exception ex) {
            log.error("exception thrown from OperationThread", ex);
        }
    }

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 110 * 60 * 1000)
    public void weChatTokenRefresh(){
        log.info("start to refresh wechat token");
        weChatAPIDomain.getWechatAccessToken(Constants.Ids.YUNSU_ORGID, weChatAppId, weChatAppSecret);
    }

}