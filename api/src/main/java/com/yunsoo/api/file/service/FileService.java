package com.yunsoo.api.file.service;

import com.yunsoo.api.client.FileApiClient;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by yan on 8/18/2016.
 */
@Service
public class FileService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private FileApiClient fileApiClient;

    public void putFile(String path, ResourceInputStream resourceInputStream) {
        Assert.hasText(path, "path must not be null or empty");
        Assert.notNull(resourceInputStream, "resourceInputStream must not be null");

        fileApiClient.put("file?path={path}", resourceInputStream, path);

        log.info(String.format("new file saved to s3 [path: %s]", path));
    }

    public ResourceInputStream getFile(String path) {
        if (path == null || path.length() < 0) {
            return null;
        }
        try {
            return fileApiClient.getResourceInputStream("file?path={path}", path);
        } catch (NotFoundException ignored) {
            log.warn("file not found in s3 " + StringFormatter.formatMap("path", path));
            return null;
        }
    }

    public List<String> getFileNamesByFolder(String folderPath) {
        return fileApiClient.get("list?path={path}", new ParameterizedTypeReference<List<String>>() {
        }, folderPath);
    }

}
