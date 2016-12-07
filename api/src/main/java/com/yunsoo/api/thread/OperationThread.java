package com.yunsoo.api.thread;

import com.yunsoo.api.cache.OperationCache;
import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.client.ThirdApiClient;
import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.api.dto.IPResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yan on 7/8/2016.
 */
@Component
public class OperationThread implements Runnable {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DataApiClient client;

    @Autowired
    private ThirdApiClient thirdApiClient;

    @Override
    public void run() {
        try {
            List<OperationLogObject> objectList = OperationCache.getAll();
            log.info("create log in operation thread, size: " + objectList.size());
            if (objectList.size() > 0) {
                objectList.forEach(object -> {
                    createLog(object);
                });
            }

        } catch (Exception e) {
            log.error("error in operation thread", e);
        }

    }

    private OperationLogObject createLog(OperationLogObject log) {
        if (log.getIp() != null) {
            IPResult ipResultObject = thirdApiClient.get("juhe/ip?ip={ip}", IPResult.class, log.getIp());
            if (ipResultObject != null)
                log.setLocation(ipResultObject.getArea());
        }


        return client.post("operation", log, OperationLogObject.class);
    }
}
