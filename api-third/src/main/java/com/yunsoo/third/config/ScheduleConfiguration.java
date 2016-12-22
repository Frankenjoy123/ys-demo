package com.yunsoo.third.config;

import com.yunsoo.third.service.WeChatService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    WeChatService weChatService;

    @Value("${yunsoo.wechat.refresh_token}")
    private Boolean weChatAutoRefreshToken;


    @Scheduled(initialDelay = 300 * 1000, fixedRate = 110 * 60 * 1000)
    public void weChatTokenRefresh(){
        if(weChatAutoRefreshToken) {
            log.info("start to refresh wechat token");
            weChatService.saveWeChatAccessTicketFromWeChat(null, null);
        }
    }

}