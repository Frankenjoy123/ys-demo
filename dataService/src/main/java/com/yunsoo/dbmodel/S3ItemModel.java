package com.yunsoo.dbmodel;

import java.io.File;

import com.amazonaws.services.s3.model.CannedAccessControlList;

/**
 * Created by Zhe on 2015/2/9.
 */
public class S3ItemModel {

    private String bucketName;
    private String itemName;
    private File file;
    private CannedAccessControlList cannedAccessControlList;

}
