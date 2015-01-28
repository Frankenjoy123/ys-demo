package com.yunsoo.nosql;

import com.amazonaws.services.dynamodbv2.model.TableDescription;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AmazonDynamoConfig.class})
public class AmazonDynamoRepoTest {

    @Autowired
    private AmazonDynamoRepo amazonDynamoRepo;

    @Test
    public void test_Test() {
        amazonDynamoRepo.Test();
    }

    @Test
    public void test_getTableDescription() throws Exception {
        TableDescription desc = amazonDynamoRepo.getTableDescription("product");
        System.out.println(desc);
        assertNotNull(desc);
    }
}