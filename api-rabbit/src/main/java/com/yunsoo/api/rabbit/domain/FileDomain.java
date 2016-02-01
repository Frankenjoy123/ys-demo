package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by:   Lijian
 * Created on:   2016-01-25
 * Descriptions:
 */
@Component
public class FileDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

//    public void putFile(String path, ResourceInputStream resourceInputStream) {
//        Assert.hasText(path, "path must not be null or empty");
//        Assert.notNull(resourceInputStream, "resourceInputStream must not be null");
//
//        dataAPIClient.put("file/s3?path={path}", resourceInputStream, path);
//
//        log.info(String.format("new file saved to s3 [path: %s]", path));
//    }

    public ResourceInputStream getFile(String path){
        return dataAPIClient.getResourceInputStream("file/s3?path={path}", path);
    }
}
