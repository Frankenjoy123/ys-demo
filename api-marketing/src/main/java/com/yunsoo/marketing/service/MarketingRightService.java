package com.yunsoo.marketing.service;

import com.yunsoo.marketing.api.util.AuthUtils;
import com.yunsoo.marketing.dao.entity.MarketingRightEntity;
import com.yunsoo.marketing.dao.repository.MarketingRightRepository;
import com.yunsoo.marketing.dto.MarketingRight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class MarketingRightService {
    @Autowired
    private MarketingRightRepository marketingRightRepository;

    public List<MarketingRight> getByMarketingId(String marketingId) {
        if (StringUtils.isEmpty(marketingId)) {
            return new ArrayList<>();
        }
        return marketingRightRepository.findByMarketingId(marketingId).stream().map(this::toMarketingRight).collect(Collectors.toList());
    }

    public MarketingRight getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MarketingRightEntity entity = marketingRightRepository.findOne(id);
        return toMarketingRight(entity);
    }

    public MarketingRight createMarketingRight(MarketingRight marketingRight) {
        MarketingRightEntity entity = new MarketingRightEntity();
        entity.setMarketingId(marketingRight.getMarketingId());
        entity.setName(marketingRight.getName());
        entity.setTypeCode(marketingRight.getTypeCode());
        entity.setAmount(marketingRight.getAmount());
        entity.setValue(marketingRight.getValue());
        entity.setDescription(marketingRight.getDescription());
        entity.setDeleted(marketingRight.getDeleted());
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        return toMarketingRight(marketingRightRepository.save(entity));
    }

    @Transactional
    public void patchUpdate(MarketingRight marketingRight) {
        if (StringUtils.isEmpty(marketingRight.getId())) {
            return;
        }
        MarketingRightEntity entity = marketingRightRepository.findOne(marketingRight.getId());
        if (entity != null) {
            if (marketingRight.getName() != null) entity.setName(marketingRight.getName());
            if (marketingRight.getTypeCode() != null) entity.setTypeCode(marketingRight.getTypeCode());
            if (marketingRight.getAmount() != null) entity.setAmount(marketingRight.getAmount());
            if (marketingRight.getValue() != null) entity.setValue(marketingRight.getValue());
            if (marketingRight.getDescription() != null) entity.setDescription(marketingRight.getDescription());
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            marketingRightRepository.save(entity);
        }
    }

    @Transactional
    public void putMarketingRightsByMarketingId(String marketingId, List<MarketingRight> marketingRights) {

        List<MarketingRightEntity> forSaveEntities = marketingRights.stream().map(m -> {
            MarketingRightEntity mre = new MarketingRightEntity();
            mre = toMarketingRightEntity(m);
            mre.setMarketingId(marketingId);
            mre.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
            mre.setCreatedDateTime(DateTime.now());
            return mre;
        }).collect(Collectors.toList());
        marketingRightRepository.save(forSaveEntities);
    }

    @Transactional
    public void deleteMarketingRight(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        MarketingRightEntity entity = marketingRightRepository.findOne(id);
        if (entity != null) {
            entity.setDeleted(true);
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            marketingRightRepository.save(entity);
        }
    }


    private MarketingRight toMarketingRight(MarketingRightEntity entity) {
        if (entity == null) {
            return null;
        }
        MarketingRight marketingRight = new MarketingRight();
        marketingRight.setId(entity.getId());
        marketingRight.setMarketingId(entity.getMarketingId());
        marketingRight.setName(entity.getName());
        marketingRight.setTypeCode(entity.getTypeCode());
        marketingRight.setAmount(entity.getAmount());
        marketingRight.setValue(entity.getValue());
        marketingRight.setDescription(entity.getDescription());
        marketingRight.setDeleted(entity.getDeleted());
        marketingRight.setCreatedAccountId(entity.getCreatedAccountId());
        marketingRight.setCreatedDateTime(entity.getCreatedDateTime());
        marketingRight.setModifiedAccountId(entity.getModifiedAccountId());
        marketingRight.setModifiedDateTime(entity.getModifiedDateTime());
        return marketingRight;
    }

    private MarketingRightEntity toMarketingRightEntity(MarketingRight object) {
        if (object == null) {
            return null;
        }
        MarketingRightEntity entity = new MarketingRightEntity();
        entity.setId(object.getId());
        entity.setMarketingId(object.getMarketingId());
        entity.setName(object.getName());
        entity.setTypeCode(object.getTypeCode());
        entity.setTypeCode(object.getTypeCode());
        entity.setAmount(object.getAmount());
        entity.setValue(object.getValue());
        entity.setValue(object.getValue());
        entity.setDeleted(object.getDeleted());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }
}
