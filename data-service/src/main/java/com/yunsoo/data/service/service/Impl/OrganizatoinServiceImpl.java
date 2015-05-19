package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/23
 * Descriptions:
 */
@Service("organizationService")
public class OrganizatoinServiceImpl implements OrganizationService {


    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public S3Object getOrgThumbnail(String bucketName, String imgKey) throws IOException {
        try {
            return s3ItemDao.getItem(bucketName, imgKey);
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode().equals("NoSuchKey")) {
                //log
            }
            return null;
        } catch (Exception ex) {
            //to-do: log
            return null;
        }
    }


}

