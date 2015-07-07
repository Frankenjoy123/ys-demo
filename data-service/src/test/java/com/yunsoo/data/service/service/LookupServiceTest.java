package com.yunsoo.data.service.service;

import com.yunsoo.data.service.config.JPAConfig;
import com.yunsoo.data.service.repository.PermissionActionRepository;
import com.yunsoo.data.service.service.contract.LookupItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2015/4/16
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {JPAConfig.class, LookupServiceTest.class})
@ComponentScan(basePackages = "com.yunsoo.data.service")
public class LookupServiceTest {

    @Autowired
    private LookupService lookupService;

    @Autowired
    PermissionActionRepository repository;


    @Test
    public void test_All() {
        LookupType lookupType = LookupType.PermissionAction;
        String code = "read";
        String name = "testName";

        LookupItem item = new LookupItem();
        item.setCode(code);
        item.setName(name);
        item.setDescription("It's a test item");
        item.setActive(false);

        System.out.println(lookupService.getAll(lookupType));
        System.out.println(lookupService.getByActive(lookupType, true));
        System.out.println(lookupService.getByCode(lookupType, code));

//
//        //result = lookupService.save(lookupType, item);
//        assert result != null;
//        System.out.println(result);
//
//        result = lookupService.getByCode(lookupType, code);
//        assert result != null : "can not find by code";
//        System.out.println(result);
//        assert name.equals(result.getName());
//
//
//        lookupService.deleteByCode(lookupType, code);
//        result = lookupService.getByCode(lookupType, code);
//        assert result == null;
//
//        System.out.println(lookupService.getAll(lookupType));
    }

}
