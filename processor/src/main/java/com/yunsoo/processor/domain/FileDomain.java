package com.yunsoo.processor.domain;

import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by:   Lijian
 * Created on:   2016-04-26
 * Descriptions:
 */
@Component
public class FileDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    public void putFile(String path, ResourceInputStream resourceInputStream) {
        Assert.hasText(path, "path must not be null or empty");
        Assert.notNull(resourceInputStream, "resourceInputStream must not be null");

        dataAPIClient.put("file/s3?path={path}", resourceInputStream, path);

        log.info(String.format("new file saved to s3 [path: %s]", path));
    }

    public ResourceInputStream getFile(String path) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path={path}", path);
        } catch (NotFoundException ignored) {
            log.warn("file not found in s3. " + StringFormatter.formatMap("path", path));
            return null;
        }
    }

    public YSFile getYSFile(String path) {
        ResourceInputStream resourceInputStream = this.getFile(path);
        if (resourceInputStream == null) {
            return null;
        }
        try {
            return YSFile.read(resourceInputStream);
        } catch (Exception e) {
            log.error("file invalid " + StringFormatter.formatMap("path", path), e);
            return null;
        }
    }
}