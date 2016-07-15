package com.yunsoo.processor.sqs.handler;

import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.processor.Application;
import com.yunsoo.processor.sqs.handler.impl.ProductPackageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2016-06-27
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port=0")
public class ProductPackageHandlerTest {

    @Autowired
    private ProductPackageHandler productPackageHandler;

    @Test
    public void test_process() {
        ProductPackageMessage message = new ProductPackageMessage();
        message.setTaskFileId("2mg9mjim4fvrteumhjd");
        productPackageHandler.process(message);

    }

}
