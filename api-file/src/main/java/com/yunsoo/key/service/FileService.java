package com.yunsoo.key.service;

import com.yunsoo.common.web.client.ResourceInputStream;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface FileService {

    ResourceInputStream getFileByPath(String path);

    void putFileToPath(String path, ResourceInputStream inputStream);

}
