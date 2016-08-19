package com.yunsoo.file.service;

import com.yunsoo.common.web.client.ResourceInputStream;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface FileService {

    ResourceInputStream getFileByPath(String path);

    void putFileToPath(String path, ResourceInputStream inputStream);

    List<String> getFileNamesByFolderName(String folderName);

}
