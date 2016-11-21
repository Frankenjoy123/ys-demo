package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/18.
 */
@Service
public class KeyAnalysisService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    public List<ProductKeyBatchObject> getDailyKeyUsageReport(String orgId, String productBaseId, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .build();
        return diApiClient.get("key/daily_usage_report" + query, new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        });
    }
}
