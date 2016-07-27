package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dake Wang on 2016/2/4.
 */

@Component
@ObjectCacheConfig
public class AnalysisDomain {

    @Autowired
    private RestClient dataApiClient;


    public List<ScanRecordAnalysisObject> getScanAnalysisReport(String orgId, org.joda.time.LocalDate startTime,  org.joda.time.LocalDate  endTime, String productBaseId, String batchId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .append("batch_id", batchId).build();
        return dataApiClient.get("analysis/scan_data" + query, new ParameterizedTypeReference<List<ScanRecordAnalysisObject>>() {
        });
    }

    public List<ScanRecordLocationAnalysisObject> getScanLocationAnalysisReport(String orgId,  org.joda.time.LocalDate  startTime,  org.joda.time.LocalDate  endTime, String productBaseId, String batchId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .append("batch_id", batchId).build();
        return dataApiClient.get("analysis/scan_data_location" + query, new ParameterizedTypeReference<List<ScanRecordLocationAnalysisObject>>() {
        });
    }

    public List<ProductKeyBatchObject> getDailyKeyUsageReport(String orgId, String productBaseId, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .build();
        return dataApiClient.get("analysis/batch_key_report" + query, new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        });
    }


    public List<MarketUserAreaAnalysisObject> getMarketUserAreaReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return dataApiClient.get("analysis/market_user_area" + query, new ParameterizedTypeReference<List<MarketUserAreaAnalysisObject>>() {
        });
    }

    public List<MarketUserDeviceAnalysisObject> getMarketUserDeviceReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return dataApiClient.get("analysis/market_user_device" + query, new ParameterizedTypeReference<List<MarketUserDeviceAnalysisObject>>() {
        });
    }

    public List<MarketUserGenderAnalysisObject> getMarketUserGenderReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return dataApiClient.get("analysis/market_user_gender" + query, new ParameterizedTypeReference<List<MarketUserGenderAnalysisObject>>() {
        });
    }

    public List<MarketUserUsageAnalysisObject> getMarketUserUsageReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return dataApiClient.get("analysis/market_user_usage" + query, new ParameterizedTypeReference<List<MarketUserUsageAnalysisObject>>() {
        });
    }

    public List<MarketUserLocationAnalysisObject> getMarketUserLocationReport(String orgId, String marketing_id, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("marketing_id", marketing_id)
                .build();
        return dataApiClient.get("analysis/market_user_location" + query, new ParameterizedTypeReference<List<MarketUserLocationAnalysisObject>>() {
        });
    }

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).TAGS.toString(), 'all')")
    public List<LuTagObject> getTags() {
        return dataApiClient.get("analysis/market_user_tags", new ParameterizedTypeReference<List<LuTagObject>>() {
        });
    }

    public EMREventReportObject getEMREventReport(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .build();

        return dataApiClient.get("analysis/user/event" + query, new ParameterizedTypeReference<EMREventReportObject>() {
        });
    }

    public List<EMREventLocationReportObject> getEMRLocationReport(String orgId, String productBaseId, String province, String city, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("province", province)
                .append("city", city)
                .append("create_datetime_start", createdDateTimeStart)
                .append("create_datetime_end", createdDateTimeEnd)
                .build();

        return dataApiClient.get("analysis/user/event/location" + query, new ParameterizedTypeReference<List<EMREventLocationReportObject>>() {
        });
    }




}
