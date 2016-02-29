package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dake Wang on 2016/2/4.
 */

@Component
public class AnalysisDomain {

    @Autowired
    private RestClient dataAPIClient;


    public List<ScanRecordAnalysisObject> getScanAnalysisReport(String orgId, org.joda.time.LocalDate startTime,  org.joda.time.LocalDate  endTime, String productBaseId, String batchId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .append("batch_id", batchId).build();
        return dataAPIClient.get("analysis/scan_data" + query, new ParameterizedTypeReference<List<ScanRecordAnalysisObject>>() {
        });
    }

    public List<ScanRecordLocationAnalysisObject> getScanLocationAnalysisReport(String orgId,  org.joda.time.LocalDate  startTime,  org.joda.time.LocalDate  endTime, String productBaseId, String batchId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .append("batch_id", batchId).build();
        return dataAPIClient.get("analysis/scan_data_location" + query, new ParameterizedTypeReference<List<ScanRecordLocationAnalysisObject>>() {
        });
    }

    public List<ProductKeyBatchObject> getDailyKeyUsageReport(String orgId, String productBaseId, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .build();
        return dataAPIClient.get("analysis/batch_key_report" + query, new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        });
    }



}
