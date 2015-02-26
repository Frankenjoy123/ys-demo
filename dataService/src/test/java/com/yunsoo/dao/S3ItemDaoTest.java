package com.yunsoo.dao;

import com.amazonaws.services.s3.model.Bucket;
import com.yunsoo.util.SpringDaoUtil;
import com.yunsoo.util.YunsooConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/13
 * Descriptions:
 */
public class S3ItemDaoTest {

    private S3ItemDao s3ItemDao;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = SpringDaoUtil.getApplicationContext();
        s3ItemDao = (S3ItemDao) applicationContext.getBean("s3ItemDao");
    }

    @Test
    public void test_listBucket() {
        List<Bucket> buckets = s3ItemDao.getBuckets();
        if (buckets != null) {
            buckets.forEach(item -> {
                System.out.println(item.getName());
            });
        }
    }


    @Test
    public void test_putItem() throws Exception {
        String bucketName = YunsooConfig.getProductKeyBatchS3bucketName();
        String key = "testKey";
        String item = "test";

        s3ItemDao.putItem(item, bucketName, key);

        String result = s3ItemDao.getItem(bucketName, key, String.class);
        System.out.println(result);
    }
}