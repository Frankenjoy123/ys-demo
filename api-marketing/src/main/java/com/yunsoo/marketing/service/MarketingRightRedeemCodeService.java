package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.MarketingRightRedeemCodeEntity;
import com.yunsoo.marketing.dao.repository.MarketingRightRedeemCodeRepository;
import com.yunsoo.marketing.dto.MarketingRightRedeemCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class MarketingRightRedeemCodeService {
    @Autowired
    private MarketingRightRedeemCodeRepository marketingRightRedeemCodeRepository;

    public MarketingRightRedeemCode getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MarketingRightRedeemCodeEntity entity = marketingRightRedeemCodeRepository.findOne(id);
        return toMarketingRightRedeemCode(entity);
    }

    public List<MarketingRightRedeemCode> getByMarketingRightId(String marketingRightId) {
        if (StringUtils.isEmpty(marketingRightId)) {
            return new ArrayList<>();
        }
        return marketingRightRedeemCodeRepository.findByMarketingRightId(marketingRightId).stream().map(this::toMarketingRightRedeemCode).collect(Collectors.toList());
    }


    private MarketingRightRedeemCode toMarketingRightRedeemCode(MarketingRightRedeemCodeEntity entity) {
        if (entity == null) {
            return null;
        }
        MarketingRightRedeemCode marketingRightRedeemCode = new MarketingRightRedeemCode();
        marketingRightRedeemCode.setId(entity.getId());
        marketingRightRedeemCode.setMarketingRightId(entity.getMarketingRightId());
        marketingRightRedeemCode.setValue(entity.getValue());
        marketingRightRedeemCode.setUsed(entity.getUsed());
        return marketingRightRedeemCode;
    }

}
