package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.WebTemplateEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by Admin on 7/19/2016.
 */
public interface WebTemplateRepository extends Repository<WebTemplateEntity, WebTemplateEntity.WebTemplatePK> {
    List<WebTemplateEntity> findByTypeCode(String typeCode);
}
