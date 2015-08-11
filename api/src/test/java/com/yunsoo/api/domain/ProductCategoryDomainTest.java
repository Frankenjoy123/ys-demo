package com.yunsoo.api.domain;

import com.yunsoo.api.config.CacheConfiguration;
import com.yunsoo.api.config.DomainConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yan on 7/27/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DomainConfiguration.class, CacheConfiguration.class})
public class ProductCategoryDomainTest {
    @Autowired
    public ProductCategoryDomain domain;

    @Test
    public void test_getProductCategories(){
        domain.getProductCategories();
        domain.getProductCategories();
        domain.getProductCategories();
        domain.getProductCategoryMap();
        domain.getProductCategoryMap();

    }
}
