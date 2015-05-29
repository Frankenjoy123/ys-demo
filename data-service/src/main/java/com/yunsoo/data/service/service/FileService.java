package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.common.data.object.FileObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhe on 2015/5/29.
 */
public interface FileService {

    public S3Object getFile(String bucket, String key) throws IOException;

    public int uploadFile(String bucketName, String key, FileObject fileObject, Boolean override) throws Exception;

}
