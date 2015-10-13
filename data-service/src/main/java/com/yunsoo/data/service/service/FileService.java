package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.common.data.object.FileObject;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/29
 * Descriptions:
 */
public interface FileService {

    S3Object getFile(String bucketName, String key);

    void putFile(String bucketName, String key, FileObject fileObject, Boolean override);

    URL getPresignedUrl(String bucketName, String key, DateTime expiration);

    List<String> getFileNamesByFolderName(String bucketName, String folderName);

}
