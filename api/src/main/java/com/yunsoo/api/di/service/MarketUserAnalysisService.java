package com.yunsoo.api.di.service;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.*;
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
@ObjectCacheConfig
public class MarketUserAnalysisService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    public List<UserProfileTagCountObject> getMarketUserAreaReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return diApiClient.get("market_user/area" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileTagCountObject> getMarketUserDeviceReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return diApiClient.get("market_user/device" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileTagCountObject> getMarketUserGenderReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return diApiClient.get("market_user/gender" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileTagCountObject> getMarketUserUsageReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return diApiClient.get("market_user/usage" + query, new ParameterizedTypeReference<List<UserProfileTagCountObject>>() {
        });
    }

    public List<UserProfileLocationCountObject> getMarketUserLocationReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return diApiClient.get("market_user/location" + query, new ParameterizedTypeReference<List<UserProfileLocationCountObject>>() {
        });
    }

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).TAGS.toString(), 'all')")
    public List<LuTagObject> getTags() {
        return diApiClient.get("market_user/tags", new ParameterizedTypeReference<List<LuTagObject>>() {
        });
    }

}
