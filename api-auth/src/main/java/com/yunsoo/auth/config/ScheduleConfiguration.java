package com.yunsoo.auth.config;

import com.yunsoo.auth.service.AccountTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
@EnableScheduling
@Configuration
public class ScheduleConfiguration {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private AccountTokenService accountTokenService;

    @Scheduled(initialDelay = 2 * 60 * 60 * 1000, fixedRate = 12 * 60 * 60 * 1000)
    public void daemon() {
        try {
            int count = accountTokenService.deleteExpiredPermanentToken();
            log.info(String.format("deleted %s expired permanent token", count));
        } catch (Exception ex) {
            log.error("exception thrown when run AccountTokenService.deleteExpiredPermanentToken", ex);
        }
    }

}
