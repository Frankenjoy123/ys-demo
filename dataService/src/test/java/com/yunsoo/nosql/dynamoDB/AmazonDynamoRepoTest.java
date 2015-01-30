package com.yunsoo.nosql.dynamoDB;

import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.yunsoo.nosql.dynamoDB.model.ProductModel;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

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

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Test
    public void test_getTableDescription() throws Exception {
        TableDescription desc = amazonDynamoRepo.getTableDescription(ProductModel.class);
        System.out.println(desc);
        assertNotNull(desc);
    }

    @Test
    public void test_putItem() {
        List<ProductModel> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductModel item = new ProductModel();
            item.setProductKey("TestBatchWrite1-" + i);
            item.setBaseProductId(1);
            item.setStatusId(1);
            item.setManufacturingDateTime(new GregorianCalendar(2014, 12, 25).getTime());
            item.setCreatedDateTime(new Date());
            items.add(item);
        }

        dynamoDBMapper.batchSave(items);
    }


}