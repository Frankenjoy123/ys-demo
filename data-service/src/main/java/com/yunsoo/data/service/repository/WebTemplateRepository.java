package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.WebTemplateEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Admin
 * Created on:   7/19/2016
 * Descriptions:
 */
public interface WebTemplateRepository extends FindOneAndSaveRepository<WebTemplateEntity, String> {

    List<WebTemplateEntity> findByTypeCode(String typeCode);

    List<WebTemplateEntity> findByTypeCodeAndRestriction(String typeCode, String restriction);
}
