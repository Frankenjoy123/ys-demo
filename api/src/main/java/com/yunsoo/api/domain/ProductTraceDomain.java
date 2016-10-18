package com.yunsoo.api.domain;

import com.yunsoo.api.client.KeyApiClient;
import com.yunsoo.api.dto.OrgAgency;
import com.yunsoo.api.dto.ProductTrace;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.OrgAgencyObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by yan on 10/14/2016.
 */
@Component
public class ProductTraceDomain {

    @Autowired
    private KeyApiClient keyApiClient;

    @Autowired
    private OrgAgencyDomain agencyDomain;

    public List<ProductTrace> getProductTraceByKey(String orgId, String partitionId, String externalKey) {
        List<ProductTrace> list = keyApiClient.get("/producttrace/external/{partitionId}/{externalKey}", new ParameterizedTypeReference<List<ProductTrace>>() {
        }, partitionId, externalKey);

        List<OrgAgencyObject> agencyList = new ArrayList<>();
        Map<String, List<String>> sourceMap = new HashMap<>();
        list.forEach(item -> {
            if (sourceMap.containsKey(item.getSourceType())) {
                if (!sourceMap.get(item.getSourceType()).contains(item.getSourceId()))
                    sourceMap.get(item.getSourceType()).add(item.getSourceId());
            } else {
                sourceMap.put(item.getSourceType(), new ArrayList<String>(Arrays.asList(item.getSourceId())));
            }
        });

        sourceMap.keySet().forEach(key -> {
            if (TraceSourceType.AGENCY.equals(key)) {
                agencyList.addAll(agencyDomain.getOrgAgencyByOrgId(orgId, null, null, sourceMap.get(key), null, null, null).getContent());
            }

        });

        list.forEach(item -> {
            if (item.getSourceType().equals(TraceSourceType.AGENCY)) {
                for (int i = 0; i < agencyList.size(); i++) {
                    OrgAgencyObject agency = agencyList.get(i);
                    if (agency.getId().equals(item.getSourceId())) {
                        item.setSourceName(agency.getName());
                        break;
                    }
                }
            }
        });

        return list;
    }

    public ProductTrace save(String partitionId, ProductTrace trace) {
        if (trace.getSourceType() == null)
            trace.setSourceType(TraceSourceType.AGENCY);
        if (trace.getAction() == null)
            trace.setAction(TraceAction.DELIVERY);

        trace.setStatusCode(LookupCodes.TraceStatus.PENDING);
        return keyApiClient.post("/producttrace/external/{partitionId}", trace, ProductTrace.class, partitionId);
    }

    public int getSum(String sourceType, String sourceId, String action, DateTime start, DateTime end) {
        if (sourceType == null)
            sourceType = TraceSourceType.AGENCY;
        if (action == null)
            action = TraceAction.DELIVERY;

        QueryStringBuilder sb = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("source_type", sourceType)
                .append("source_id", sourceId)
                .append("action", action);
        if (start != null)
            sb.append("created_datetime_begin", start);
        if (end != null)
            sb.append("created_datetime_end", end);
        return keyApiClient.get("/producttrace/sum" + sb.build(), Integer.class);
    }

    private static class TraceSourceType {
        public static String AGENCY = "agency";
    }

    private static class TraceAction {
        public static String DELIVERY = "delivery";
    }

}
