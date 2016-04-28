package com.yunsoo.processor.sqs.handler;

/**
 * Created by:   Lijian
 * Created on:   2016-04-26
 * Descriptions:
 */
public interface MessageHandler<T> {

    void process(T message);


    /**
     * @return millisecond of timeout for the task executing
     */
    long getTimeout();

}
