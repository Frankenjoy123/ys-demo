package com.yunsoo.api.thread;

import com.yunsoo.api.cache.OperationCache;
import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.client.RestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by yan on 7/8/2016.
 */
public class OperationThread implements Runnable {

    private Log log = LogFactory.getLog(this.getClass());
    private RestClient client;


    public OperationThread(@Value("${yunsoo.client.dataapi.baseurl}") String baseUrl) {
        super();
        client = new DataAPIClient(baseUrl);
        log.info("operation thread rest client base url:" + baseUrl);
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<OperationLogObject> objectList = OperationCache.getAll();
                log.info("create log in operation thread, size: " + objectList.size());
                if(objectList.size() >0) {
                    objectList.forEach(object -> {
                        createLog(object);
                    });
                }
                else {
                    log.info("begin to sleep 20 min");
                    Thread.sleep(1000 * 60);
                }

            } catch (Exception e) {
                log.error("error in operation thread", e);
            }
        }
    }

    private OperationLogObject createLog(OperationLogObject log) {
        return client.post("operation", log, OperationLogObject.class);
    }
}
