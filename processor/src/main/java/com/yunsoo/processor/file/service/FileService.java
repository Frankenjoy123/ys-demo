package com.yunsoo.processor.file.service;

import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.processor.client.FileApiClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
@Service
public class FileService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private FileApiClient fileApiClient;

    public ResourceInputStream getFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        try {
            return fileApiClient.getResourceInputStream("file?path={path}", path);
        } catch (NotFoundException ignored) {
            log.warn("file not found by path: " + path);
            return null;
        }
    }

    public void putFile(String path, ResourceInputStream resourceInputStream) {
        Assert.hasText(path, "path must not be null or empty");
        Assert.notNull(resourceInputStream, "resourceInputStream must not be null");

        fileApiClient.put("file?path={path}", resourceInputStream, path);

        log.info("file saved to path: " + path);
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
