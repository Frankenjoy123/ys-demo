package com.yunsoo.di.service;

import com.yunsoo.di.dao.entity.ProductKeyBatchEntity;
import com.yunsoo.di.dao.repository.ProductKeyBatchRepository;
import com.yunsoo.di.dto.ProductKeyBatch;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yqy09_000 on 2016/9/10.
 */
@Service
public class KeyService {

    @Autowired
    ProductKeyBatchRepository keyBatchRepository;

   public List<ProductKeyBatch> getDailyKeyUsageReport(String orgId, String packageBaseId, DateTime fromTime, DateTime toTime) {
        List<ProductKeyBatchEntity> entities = keyBatchRepository.queryDailyKeyUsageReport(orgId, packageBaseId, fromTime, toTime);
        return entities.stream().map(KeyService::toProductKeyBatch).collect(Collectors.toList());
    }

    private static ProductKeyBatch toProductKeyBatch(ProductKeyBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(entity.getId());
        batch.setBatchNo(entity.getBatchNo());
        batch.setQuantity(entity.getQuantity());
        batch.setStatusCode(entity.getStatusCode());
        batch.setOrgId(entity.getOrgId());
        batch.setProductBaseId(entity.getProductBaseId());
        batch.setCreatedAppId(entity.getCreatedAppId());
        batch.setCreatedDeviceId(entity.getCreatedDeviceId());
        batch.setCreatedAccountId(entity.getCreatedAccountId());
        batch.setCreatedDateTime(entity.getCreatedDateTime());
        String codes = entity.getProductKeyTypeCodes();
        if (codes != null) {
            batch.setProductKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(codes)));
        }
        batch.setMarketingId(entity.getMarketingId());
        return batch;


    }

}
