package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ApplicationEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/6/15
 * Descriptions:
 */
public interface ApplicationRepository extends FindOneAndSaveRepository<ApplicationEntity, String> {

    Page<ApplicationEntity> findAll(Pageable pageable);

    Page<ApplicationEntity> findByTypeCode(String type_code, Pageable pageable);

    Page<ApplicationEntity> findByStatusCodeIn(List<String> statusCodes, Pageable pageable);

    Page<ApplicationEntity> findByTypeCodeAndStatusCodeIn(String type_code, List<String> statusCodes, Pageable pageable);

}
