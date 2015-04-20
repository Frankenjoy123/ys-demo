package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.config.JPAConfig;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JPAConfig.class)
public class ProductKeyBatchRepositoryTest {

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;

    @Test
    public void test_getById() {
        ProductKeyBatchEntity e = new ProductKeyBatchEntity();
        e.setQuantity(1);
        e.setStatusCode("ss");
        e.setProductKeyTypeCodes("1,2");
        e.setProductBaseId("1");
        e.setOrgId("1");
        e.setCreatedAccountId("1");
        e.setCreatedAppId("1");
        e.setCreatedDateTime(DateTime.now());
        productKeyBatchRepository.save(e);
        System.out.println(e.getId());
        System.out.println(productKeyBatchRepository.findOne(e.getId()).getId());
    }
}
