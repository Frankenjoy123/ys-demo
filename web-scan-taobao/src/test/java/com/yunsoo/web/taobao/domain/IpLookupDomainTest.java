package com.yunsoo.web.taobao.domain;

import com.yunsoo.web.taobao.config.DomainConfiguration;
import com.yunsoo.web.taobao.dto.BaiduIpLookupResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DomainConfiguration.class})
public class IpLookupDomainTest {

    @Autowired
    private IpLookupDomain ipLookupDomain;

    @Test
    public void test_ipLookup() {
        String ip = "115.195.44.32";
        BaiduIpLookupResult result = ipLookupDomain.ipLookup(ip);

    }

}
