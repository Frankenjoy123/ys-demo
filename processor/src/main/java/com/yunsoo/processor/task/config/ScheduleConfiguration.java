package com.yunsoo.processor.task.config;

import com.yunsoo.processor.task.TaskProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduleConfiguration {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private TaskProcessor taskProcessor;

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 10 * 1000)
    public void daemon() {
        try {
            taskProcessor.process();
        } catch (Exception ex) {
            log.error("exception thrown from TaskProcessor", ex);
        }
    }

}
