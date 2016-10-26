package com.yunsoo.processor.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.processor.client.KeyApiClient;
import com.yunsoo.processor.key.dto.ProductTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 10/17/2016.
 */
@Component
public class ProductTraceDomain {

    @Autowired
    KeyApiClient keyApiClient;

    private static final int SIZE = 500;

    public List<ProductTrace> getPendingTraceList(){
        return keyApiClient.get("/producttrace?status_code={status}&page=0&size={size}", new ParameterizedTypeReference<List<ProductTrace>>() {
        }, LookupCodes.TraceStatus.PENDING, SIZE);
    }

    public void updateAfterSynch(List<ProductTrace> traceList){
        traceList.forEach(item -> item.setStatusCode(LookupCodes.TraceStatus.SYNCHRONIZED));
        keyApiClient.post("/producttrace/keys", traceList, null);
    }


}
