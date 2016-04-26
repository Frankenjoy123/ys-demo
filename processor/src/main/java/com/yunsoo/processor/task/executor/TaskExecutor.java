package com.yunsoo.processor.task.executor;

import com.yunsoo.processor.task.Task;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
public interface TaskExecutor {

    /**
     * main logic of execution
     */
    void execute(Task task);

    /**
     * default timeout
     *
     * @return millisecond of timeout for the task executing
     */
    long getTimeout();

}
