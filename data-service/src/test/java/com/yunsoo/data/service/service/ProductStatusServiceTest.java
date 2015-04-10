package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.ProductStatusService;
import com.yunsoo.data.service.service.contract.ProductStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml.bc"})
public class ProductStatusServiceTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ProductStatusService productStatusService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetById() throws Exception {
        ProductStatus productStatus = productStatusService.getById(1);
        assertNotNull(productStatus);
    }

    @Test
    public void testSave() throws Exception {
        ProductStatus productStatus = new ProductStatus();
        productStatus.setActive(true);
        productStatus.setCode("Code145");
        productStatus.setDescription("产品测试状态100");
        int result = productStatusService.save(productStatus);
        assertTrue(result > 0);
    }

    @Test
    public void testPatchUpdate() throws Exception {
        ProductStatus productStatus = new ProductStatus();
        productStatus.setId(6);
        productStatus.setDescription("产品测试状态100 - by merge4");
        assertTrue(productStatusService.patchUpdate(productStatus));
    }

    @Test
    public void testUpdate() throws Exception {
        ProductStatus productStatus = productStatusService.getById(6);
        assertNotNull(productStatus);
        productStatus.setDescription("产品测试状态100 - by merge5");
        assertTrue(productStatusService.update(productStatus));
    }

    @Test
    public void testDelete() throws Exception {
        assertTrue(productStatusService.delete(6));
    }

    @Test
    public void testGetAllProductKeyStatus() throws Exception {

    }
}