package com.yunsoo.processor.task.executor.impl;

import com.yunsoo.processor.task.Task;
import com.yunsoo.processor.task.annotation.Executor;
import com.yunsoo.processor.task.executor.TaskExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@Executor("HealthCheck")
public class HealthCheckTaskExecutor implements TaskExecutor {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void execute(Task task) {
//        List<String> orderIds = orderService.getNotClosedOrderIds();
//        log.info(String.format("found %d orders not closed", orderIds.size()));
//
//        int successCount = 0;
//        for (String id : orderIds) {
//            try {
//                if (orderService.close(id)) {
//                    successCount++;
//                }
//            } catch (Exception ex) {
//                log.error(String.format("exception during processing order %s", id), ex);
//            }
//        }
//        if (successCount > 0) {
//            log.info(String.format("closed %d orders", successCount));
//        }
    }

    @Override
    public long getTimeout() {
        return 300 * 1000; //300s
    }
}