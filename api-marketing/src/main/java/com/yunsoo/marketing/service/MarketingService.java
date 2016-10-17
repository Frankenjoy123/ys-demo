package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.MarketingEntity;
import com.yunsoo.marketing.dao.repository.MarketingRepository;
import com.yunsoo.marketing.dto.Marketing;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class MarketingService {

    @Autowired
    private MarketingRepository marketingRepository;

    public Marketing getMarketingById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MarketingEntity entity = marketingRepository.findOne(id);
        return toMarketing(entity);
    }

    public Marketing createMarketing(Marketing marketing) {
        MarketingEntity entity = new MarketingEntity();
        entity.setOrgId(marketing.getOrgId());
        entity.setName(marketing.getName());
        entity.setTypeCode(marketing.getTypeCode());
        entity.setStatusCode(marketing.getStatusCode());
        entity.setTemplateId(marketing.getTemplateId());
        entity.setBudget(marketing.getBudget());
        entity.setBalance(marketing.getBalance());
        entity.setComments(marketing.getComments());
        entity.setCreatedAccountId(marketing.getCreatedAccountId());
        entity.setCreatedDateTime(DateTime.now());
        return toMarketing(marketingRepository.save(entity));
    }

    private Marketing toMarketing(MarketingEntity entity) {
        if (entity == null) {
            return null;
        }
        Marketing marketing = new Marketing();
        marketing.setId(entity.getId());
        marketing.setOrgId(entity.getOrgId());
        marketing.setName(entity.getName());
        marketing.setStatusCode(entity.getStatusCode());
        marketing.setTemplateId(entity.getTemplateId());
        marketing.setBudget(entity.getBudget());
        marketing.setBalance(entity.getBalance());
        marketing.setComments(entity.getComments());
        marketing.setCreatedAccountId(entity.getCreatedAccountId());
        marketing.setCreatedDateTime(entity.getCreatedDateTime());
        marketing.setModifiedAccountId(entity.getModifiedAccountId());
        marketing.setModifiedDateTime(entity.getModifiedDateTime());
        return marketing;
    }

}
