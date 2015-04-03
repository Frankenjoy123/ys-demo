package com.yunsoo.amazon.dynamoDB;

import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.yunsoo.dbmodel.ProductModel;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AmazonDynamoDBConfig.class})
public class AmazonDynamoDBRepoTest {

    @Autowired
    private AmazonDynamoDBRepo amazonDynamoDBRepo;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Test
    public void test_getTableDescription() throws Exception {
        TableDescription desc = amazonDynamoDBRepo.getTableDescription(ProductModel.class);
        System.out.println(desc);
        assertNotNull(desc);
    }

}