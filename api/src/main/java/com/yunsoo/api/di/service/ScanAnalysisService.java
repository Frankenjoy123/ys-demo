package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.api.di.dto.DrawAnalysisReport;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by xiaowu on 2016/11/18.
 */
@Service
public class ScanAnalysisService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    public List<ScanRecordAnalysisObject> getScanAnalysisReport(String orgId, org.joda.time.LocalDate startTime,  org.joda.time.LocalDate  endTime, String productBaseId, String batchId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .append("batch_id", batchId).build();
        return diApiClient.get("scan/scan_data" + query, new ParameterizedTypeReference<List<ScanRecordAnalysisObject>>() {
        });
    }

    public List<ScanRecordLocationAnalysisObject> getScanLocationAnalysisReport(String orgId,  org.joda.time.LocalDate  startTime,  org.joda.time.LocalDate  endTime, String productBaseId, String batchId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("start_time", startTime).append("end_time", endTime).append("product_base_id", productBaseId)
                .append("batch_id", batchId).build();
        return diApiClient.get("scan/scan_data_location" + query, new ParameterizedTypeReference<List<ScanRecordLocationAnalysisObject>>() {
        });
    }
}
