package com.yunsoo.file.service.impl;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.file.service.FileService;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public class SimpleFileServiceImpl implements FileService {

    private String basePath;

    public SimpleFileServiceImpl(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public ResourceInputStream getFileByPath(String path, String bucketName) {
        return null;
    }

    @Override
    public void putFileToPath(String path, ResourceInputStream inputStream) {

    }

    @Override
    public List<String> getFileNamesByFolderName(String folderName){
        return null;
    }

}
