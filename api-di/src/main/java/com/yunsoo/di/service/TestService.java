package com.yunsoo.di.service;

import com.yunsoo.di.dao.entity.TestEntity;
import com.yunsoo.di.dao.repository.TestRepository;
import com.yunsoo.di.dto.TestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yqy09_000 on 2016/9/10.
 */
@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    public TestDto getTestData(long id)
    {
        TestEntity entity = testRepository.findOne(id);
        return toTestDto(entity);
    }

    private TestDto toTestDto(TestEntity entity) {
        TestDto dto = new TestDto();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        return dto;
    }
}
