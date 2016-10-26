package com.yunsoo.processor.task.executor.impl;

import com.yunsoo.processor.domain.ProductTraceDomain;
import com.yunsoo.processor.key.dto.ProductTrace;
import com.yunsoo.processor.task.Task;
import com.yunsoo.processor.task.annotation.Executor;
import com.yunsoo.processor.task.executor.TaskExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by yan on 10/17/2016.
 */
@Executor("ProductTraceManage")
public class ProductTraceTaskExecutor implements TaskExecutor {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    ProductTraceDomain domain;

    @Override
    public void execute(Task task) {
        List<ProductTrace> traceList = domain.getPendingTraceList();
        if(traceList.size()>0){
            log.info(String.format("start processing %d task files", traceList.size()));
            domain.updateAfterSynch(traceList);
        }
    }

    @Override
    public long getTimeout() {
        return 3000 * 1000; //300s
    }
}
