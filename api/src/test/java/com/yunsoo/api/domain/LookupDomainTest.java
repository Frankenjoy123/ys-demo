package com.yunsoo.api.domain;

import com.yunsoo.api.config.CacheConfiguration;
import com.yunsoo.api.config.DomainConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/2
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DomainConfiguration.class, CacheConfiguration.class})
public class LookupDomainTest {

    @Autowired
    private LookupDomain lookupDomain;

    @Test
    public void test_getProductKeyTypes() {
        lookupDomain.getProductKeyTypes();
        lookupDomain.getProductKeyTypes();
        lookupDomain.getProductKeyTypes(true);
        lookupDomain.getProductKeyTypes(true);
    }
}
