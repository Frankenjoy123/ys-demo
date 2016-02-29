package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;

import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/29
 * Descriptions:
 */
public interface FileService {

    S3Object getFile(String bucketName, String key);

    List<String> getFileNamesByFolderName(String bucketName, String folderName);

}
