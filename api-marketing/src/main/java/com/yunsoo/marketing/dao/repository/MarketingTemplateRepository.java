package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.MarketingTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface MarketingTemplateRepository extends Repository<MarketingTemplateEntity, String> {
    MarketingTemplateEntity findOne(String id);

    MarketingTemplateEntity save(MarketingTemplateEntity entity);

    Page<MarketingTemplateEntity> findByOrgId(String orgId, Pageable pageable);

}
