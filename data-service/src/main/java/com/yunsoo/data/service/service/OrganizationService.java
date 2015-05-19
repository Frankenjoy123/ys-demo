package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/23
 * Descriptions:
 */
public interface OrganizationService {

    S3Object getOrgThumbnail(String bucketName, String imgKey) throws IOException;

}
