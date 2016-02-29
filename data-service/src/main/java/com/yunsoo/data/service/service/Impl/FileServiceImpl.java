package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/29
 * Descriptions:
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public S3Object getFile(String bucketName, String key) {
        try {
            return s3ItemDao.getItem(bucketName, key);
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getStatusCode() == 404) {
                return null;
            } else {
                throw s3ex;
            }
        }
    }

    @Override
    public List<String> getFileNamesByFolderName(String bucketName, String folderName){
        try {
            List<String> nameList = s3ItemDao.getItemNamesByFolderName(bucketName, folderName);
            return nameList.stream().map(this::getFileShortName).collect(Collectors.toList());
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getStatusCode() == 404) {
                return new ArrayList<>();
            } else {
                throw s3ex;
            }
        }
    }

    private String getFileShortName(String fileFullName){
        String[] names = fileFullName.split("/");
        return names[names.length -1];
    }

}
