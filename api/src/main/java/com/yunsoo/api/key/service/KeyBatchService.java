package com.yunsoo.api.key.service;

import com.yunsoo.api.client.KeyApiClient;
import com.yunsoo.api.key.dto.KeyBatch;
import com.yunsoo.api.key.dto.KeyBatchCreationRequest;
import com.yunsoo.api.key.dto.Keys;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-11-03
 * Descriptions:
 */
@Service
public class KeyBatchService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;

    public Page<KeyBatch> getByFilter(String orgId, String productBaseId, List<String> statusCodeIn, String createdAccountId, DateTime createdDateTimeGE, DateTime createdDateTimeLE, Pageable pageable) {
        if (StringUtils.isEmpty(orgId)) {
            return Page.empty();
        }
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("status_code_in", statusCodeIn)
                .append("created_account_id", createdAccountId)
                .append("created_datetime_ge", createdDateTimeGE)
                .append("created_datetime_le", createdDateTimeLE)
                .append(pageable)
                .build();
        return keyApiClient.getPaged("keyBatch" + query, new ParameterizedTypeReference<List<KeyBatch>>() {
        });
    }

    public List<String> getPartitionIdsByOrgId(String orgId) {
        return getByFilter(orgId, null, null, null, null, null, null)
                .getContent()
                .stream()
                .map(KeyBatch::getPartitionId)
                .filter(id -> id != null && id.length() > 0)
                .distinct()
                .collect(Collectors.toList());
    }

    public Keys getKeysByKeyBatchId(String keyBatchId) {
        if (StringUtils.isEmpty(keyBatchId)) {
            return null;
        }
        try {
            return keyApiClient.get("keyBatch/{id}/keys", Keys.class, keyBatchId);
        } catch (NotFoundException e) {
            log.warn("keys not found by keyBatchId: " + keyBatchId);
            return null;
        }
    }

    public KeyBatch create(KeyBatchCreationRequest request) {
        return keyApiClient.post("keyBatch", request, KeyBatch.class);
    }

    /**
     * update batchNo, productBaseId, statusCode
     *
     * @param keyBatch
     */
    public void patchUpdateKeyBatch(KeyBatch keyBatch) {
        keyApiClient.patch("keyBatch/{id}", keyBatch, keyBatch.getId());
    }

}
