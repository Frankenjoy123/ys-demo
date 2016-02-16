package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.Application;
import com.yunsoo.data.service.entity.OrganizationEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/9/18
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    public void test_findByName() {
        String name = "云溯科技";
        List<OrganizationEntity> organizationEntities = organizationRepository.findByName(name);
        assert organizationEntities.size() == 1 : "organization not found by [name: " + name + "]";
    }
}
