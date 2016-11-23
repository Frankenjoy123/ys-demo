package com.yunsoo.api.di.service;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.LuTagObject;
import com.yunsoo.common.data.object.MarketWinUserLocationAnalysisObject;
import com.yunsoo.common.data.object.UserProfileLocationCountObject;
import com.yunsoo.common.data.object.UserProfileTagCountObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/18.
 */
@Service
public class MarketingService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    public List<MarketWinUserLocationAnalysisObject> getMarketWinUserLocationReportByMarketingId(String marketingId) {

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("marketing_id", marketingId)
                .build();
        return diApiClient.get("market_user/win_location" + query, new ParameterizedTypeReference<List<MarketWinUserLocationAnalysisObject>>() {
        });
    }
}
