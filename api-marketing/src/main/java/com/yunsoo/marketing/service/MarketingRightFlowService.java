package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.entity.MarketingRightFlowEntity;
import com.yunsoo.marketing.dao.repository.MarketingRightFlowRepository;
import com.yunsoo.marketing.dto.MarketingRightFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class MarketingRightFlowService {
    @Autowired
    private MarketingRightFlowRepository marketingRightFlowRepository;

    public MarketingRightFlow getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MarketingRightFlowEntity entity = marketingRightFlowRepository.findOne(id);
        return toMarketingRightFlow(entity);
    }

    private MarketingRightFlow toMarketingRightFlow(MarketingRightFlowEntity entity) {
        if (entity == null) {
            return null;
        }
        MarketingRightFlow marketingRightFlow = new MarketingRightFlow();
        marketingRightFlow.setId(entity.getId());
        marketingRightFlow.setAmount(entity.getAmount());
        marketingRightFlow.setCost(entity.getCost());
        marketingRightFlow.setCmccFlowId(entity.getCmccFlowId());
        marketingRightFlow.setCuccFlowId(entity.getCuccFlowId());
        marketingRightFlow.setCtccFlowId(entity.getCtccFlowId());
        marketingRightFlow.setComments(entity.getComments());
        return marketingRightFlow;
    }

}
