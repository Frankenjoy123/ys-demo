package com.yunsoo.marketing.service;

import com.yunsoo.common.web.client.Page;
import com.yunsoo.marketing.api.util.AuthUtils;
import com.yunsoo.marketing.api.util.PageUtils;
import com.yunsoo.marketing.dao.entity.MarketingEntity;
import com.yunsoo.marketing.dao.repository.MarketingRepository;
import com.yunsoo.marketing.dto.Marketing;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private MarketingRightService marketingRightService;

    public Marketing getMarketingById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MarketingEntity entity = marketingRepository.findOne(id);
        return toMarketing(entity);
    }

    @Transactional
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
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        return toMarketing(marketingRepository.save(entity));
    }

    @Transactional
    public void patchUpdate(Marketing marketing) {
        if (StringUtils.isEmpty(marketing.getId())) {
            return;
        }
        MarketingEntity entity = marketingRepository.findOne(marketing.getId());
        if (entity != null) {
            if (marketing.getName() != null) entity.setName(marketing.getName());
            if (marketing.getTypeCode() != null) entity.setTypeCode(marketing.getTypeCode());
            if (marketing.getTemplateId() != null) entity.setTemplateId(marketing.getTemplateId());
            if (marketing.getBudget() != null) entity.setBudget(marketing.getBudget());
            if (marketing.getBalance() != null) entity.setBalance(marketing.getBalance());
            if (marketing.getComments() != null) entity.setComments(marketing.getComments());
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            marketingRepository.save(entity);
        }
    }

    public Page<Marketing> getByOrgId(String orgId, Pageable pageable) {
        if (StringUtils.isEmpty(orgId)) {
            return Page.empty();
        }
        return PageUtils.convert(marketingRepository.findByOrgId(orgId, pageable)).map(this::toMarketing);
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
