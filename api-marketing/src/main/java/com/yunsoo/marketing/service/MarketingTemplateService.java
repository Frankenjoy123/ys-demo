package com.yunsoo.marketing.service;

import com.yunsoo.common.web.client.Page;
import com.yunsoo.marketing.api.util.PageUtils;
import com.yunsoo.marketing.dao.entity.MarketingTemplateEntity;
import com.yunsoo.marketing.dao.repository.MarketingTemplateRepository;
import com.yunsoo.marketing.dto.MarketingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class MarketingTemplateService {
    @Autowired
    private MarketingTemplateRepository marketingTemplateRepository;

    public MarketingTemplate getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MarketingTemplateEntity entity = marketingTemplateRepository.findOne(id);
        return toMarketingTemplate(entity);
    }

    @Transactional
    public void patchUpdate(MarketingTemplate marketingTemplate) {
        if (StringUtils.isEmpty(marketingTemplate.getId())) {
            return;
        }
        MarketingTemplateEntity entity = marketingTemplateRepository.findOne(marketingTemplate.getId());
        if (entity != null) {
            if (marketingTemplate.getName() != null) entity.setName(marketingTemplate.getName());
            if (marketingTemplate.getTypeCode() != null) entity.setTypeCode(marketingTemplate.getTypeCode());
            if (marketingTemplate.getStatusCode() != null) entity.setStatusCode(marketingTemplate.getStatusCode());
            if (marketingTemplate.getDescription() != null) entity.setDescription(marketingTemplate.getDescription());
            marketingTemplateRepository.save(entity);
        }
    }

    public Page<MarketingTemplate> getByOrgId(String orgId, Pageable pageable) {
        if (StringUtils.isEmpty(orgId)) {
            return Page.empty();
        }
        return PageUtils.convert(marketingTemplateRepository.findByOrgId(orgId, pageable)).map(this::toMarketingTemplate);
    }


    private MarketingTemplate toMarketingTemplate(MarketingTemplateEntity entity) {
        if (entity == null) {
            return null;
        }
        MarketingTemplate marketingTemplate = new MarketingTemplate();
        marketingTemplate.setId(entity.getId());
        marketingTemplate.setName(entity.getName());
        marketingTemplate.setTypeCode(entity.getTypeCode());
        marketingTemplate.setStatusCode(entity.getStatusCode());
        marketingTemplate.setOrgId(entity.getOrgId());
        marketingTemplate.setDescription(entity.getDescription());
        return marketingTemplate;
    }

}
