package com.yunsoo.processor.task;

import com.yunsoo.processor.domain.LogDomain;
import com.yunsoo.processor.task.executor.TaskExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */

@Component
public class TaskProcessor {
    private static final long MAX_PROCESSING_COUNT = 8;
    private static final long DEFAULT_TIMEOUT = 10 * 60 * 1000; //10m

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private Worker worker;


    public void process() {
        List<Task> tasks = taskService.getRunnable();
        if (tasks.size() > 0) {
            log.info(String.format("processor found %d available tasks", tasks.size()));
            for (int i = 0; i < tasks.size() && i < MAX_PROCESSING_COUNT; i++) {
                Task task = tasks.get(i);
                String taskCode = task.getCode();

                TaskExecutor executor = getTaskExecutor(task);
                if (executor == null) {
                    log.warn(String.format("no corresponding executor bean found for task {code: %s, executor: %s}", taskCode, task.getExecutor()));
                    continue; //no corresponding executor bean found in the context
                }

                long timeout = executor.getTimeout() > 0 ? executor.getTimeout() : DEFAULT_TIMEOUT;
                task = taskService.lock(taskCode, timeout);
                if (task == null) {
                    log.warn(String.format("lock task {code: %s} failed", taskCode));
                    continue; //lock failed and try next task
                }

                //async execute task
                worker.executeAsync(executor, task);
            }
        }
    }

    private TaskExecutor getTaskExecutor(Task task) {
        try {
            return ctx.getBean(task.getExecutor(), TaskExecutor.class);
        } catch (Exception ex) {
            return null;
        }
    }


    @Component
    public static class Worker {

        private Log log = LogFactory.getLog(getClass());

        @Autowired
        private LogDomain logDomain;

        @Autowired
        private TaskService taskService;

        @Async
        public void executeAsync(TaskExecutor executor, Task task) {
            String taskCode = task.getCode();
            try {
                log.info(String.format("task {code: %s} start executing", taskCode));
                DateTime start = DateTime.now();

                executor.execute(task);

                DateTime end = DateTime.now();
                long duration = (end.getMillis() - start.getMillis()) / 1000;

                taskService.saveFinished(taskCode);

                log.info(String.format("task {code: %s} finished successfully in %d seconds", taskCode, duration));
                logDomain.logInfo("task_processor", String.format("task finished successfully in %d seconds", duration), taskCode, "task_code");
            } catch (Exception ex) {
                taskService.saveFailed(taskCode, ex.getMessage());

                log.error(String.format("task {code: %s} failed with error: %s", taskCode, ex.getMessage()), ex);
                logDomain.logError("task_processor", "task failed with error: " + ex.getMessage(), taskCode, "task_code");
            }
        }
    }
}
