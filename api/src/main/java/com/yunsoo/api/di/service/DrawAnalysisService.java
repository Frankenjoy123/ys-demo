package com.yunsoo.api.di.service;

import com.yunsoo.api.client.DiApiClient;
import com.yunsoo.api.di.dto.DrawAnalysisReport;
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
 * Created by yqy09_000 on 2016/11/14.
 */
@Service
public class DrawAnalysisService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DiApiClient diApiClient;

    /**
     * 返回对应的数据统计，按时间维度和营销维度
     * @param orgId optional 可传可不传，一般都要求传，奥瑞金可以bypass
     * @param marketingId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<DrawAnalysisReport> getDrawAnalysisReport(String orgId, String marketingId, boolean orgByPass, LocalDate startDate, LocalDate endDate) {
        if (StringUtils.isEmpty(marketingId)) {
            return null;
        }

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("m_id", marketingId).append("by", orgByPass).append("ds", startDate).append("de", endDate)
                .build();
        return diApiClient.get("draw_analysis" + query, new ParameterizedTypeReference<List<DrawAnalysisReport>>() {
        });

    }
}
