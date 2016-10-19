package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.MarketingTemplateEntity;
import com.yunsoo.marketing.dao.repository.MarketingTemplateRepository;
import com.yunsoo.marketing.dto.MarketingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
