package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.MarketingRightEntity;
import com.yunsoo.marketing.dao.repository.MarketingRightRepository;
import com.yunsoo.marketing.dto.MarketingRight;
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

    @Transactional
    public void putMarketingRightsByMarketingId(String marketingId, List<MarketingRight> marketingRights) {
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
}
