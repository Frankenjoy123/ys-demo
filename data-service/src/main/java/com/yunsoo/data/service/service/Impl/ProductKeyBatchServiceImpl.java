package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.data.service.config.AWSConfigProperties;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import com.yunsoo.data.service.service.ProductKeyBatchService;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Service("productKeyBatchService")
public class ProductKeyBatchServiceImpl implements ProductKeyBatchService {

    private static final int KEY_LENGTH = 22;
    private static final byte[] DELIMITER1 = new byte[]{0x2C}; //,
    private static final byte[] DELIMITER2 = new byte[]{0x3B}; //;

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;

    @Autowired
    private S3ItemDao s3ItemDao;

    @Autowired
    private AWSConfigProperties awsConfigProperties;


    @Override
    public ProductKeyBatch getById(String id) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(id);
        return toProductKeyBatch(entity);
    }

    @Override
    public ProductKeys getProductKeysByBatchId(String batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);
        if (keyBatch == null || keyBatch.getProductKeysUri() == null) {
            return null;
        }
        List<List<String>> keys = getProductKeyListFromS3(keyBatch.getProductKeysUri());
        if (keys == null) {
            return null;
        }
        ProductKeys productKeys = new ProductKeys();
        productKeys.setBatchId(keyBatch.getId());
        productKeys.setQuantity(keyBatch.getQuantity());
        productKeys.setCreatedDateTime(keyBatch.getCreatedDateTime());
        productKeys.setProductKeyTypeCodes(keyBatch.getProductKeyTypeCodes());
        productKeys.setProductKeys(keys);
        return productKeys;
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch batch) {
        int quantity = batch.getQuantity();
        List<String> keyTypeCodes = batch.getProductKeyTypeCodes();

        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
        Assert.isTrue(keyTypeCodes.size() > 0, "productKeyTypeCodes must not be empty");

        //generate productKeys
        List<List<String>> keyList = generateProductKeys(batch);

        //save batch
        batch.setId(null); //set to null for creating new item
        if (batch.getCreatedDateTime() == null) {
            batch.setCreatedDateTime(DateTime.now());
        }
        ProductKeyBatchEntity newEntity = productKeyBatchRepository.save(toProductKeyBatchEntity(batch));

        //save product keys to S3
        String uri = saveProductKeyListToS3(
                newEntity.getOrgId(),
                newEntity.getId(),
                quantity,
                keyTypeCodes.size(),
                keyList);

        //update batch with uri
        newEntity.setProductKeysUri(uri);
        newEntity = productKeyBatchRepository.save(newEntity);

        return toProductKeyBatch(newEntity);
    }

    @Override
    public void patchUpdate(ProductKeyBatch batch) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(batch.getId());
        if (entity != null) {
            if (batch.getOrgId() != null) {
                entity.setOrgId(batch.getOrgId());
            }
            if (batch.getProductBaseId() != null) {
                entity.setProductBaseId(batch.getProductBaseId());
            }
            if (batch.getStatusCode() != null) {
                entity.setStatusCode(batch.getStatusCode());
            }
            productKeyBatchRepository.save(entity);
        }
    }


    //private methods

    private List<List<String>> generateProductKeys(ProductKeyBatch batch) {
        int quantity = batch.getQuantity();
        List<String> keyTypeCodes = batch.getProductKeyTypeCodes();

        List<List<String>> keyList = new ArrayList<>(quantity);

        for (int i = 0, len = keyTypeCodes.size(); i < quantity; i++) {
            List<String> tempKeys = new ArrayList<>(len);
            for (int j = 0; j < len; j++) {
                tempKeys.add(KeyGenerator.getNew());
            }
            keyList.add(tempKeys);
        }

        return keyList;
    }

    private String saveProductKeyListToS3(String orgId,
                                          String batchId,
                                          int quantity,
                                          int productKeyTypeCodesCount,
                                          List<List<String>> keyList) {
        int totalKeyLength = (KEY_LENGTH + 1) * productKeyTypeCodesCount * quantity;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(totalKeyLength);
        for (int i = 0; i < quantity; i++) {
            List<String> keys = keyList.get(i);
            for (int j = 0; j < productKeyTypeCodesCount; j++) {
                byte[] temp = keys.get(j).getBytes(StandardCharsets.UTF_8);
                byteArrayOutputStream.write(temp, 0, KEY_LENGTH);
                if (j < productKeyTypeCodesCount - 1) {
                    byteArrayOutputStream.write(DELIMITER1, 0, 1);
                }
            }
            byteArrayOutputStream.write(DELIMITER2, 0, 1);
        }

        String bucketName = awsConfigProperties.getS3().getBucketName();
        String key = String.format("organization/%s/product_key_batch/%s", orgId, batchId);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(totalKeyLength);
        s3ItemDao.putItem(bucketName, key, inputStream, metadata);

        return String.join("/", bucketName, key); //uri
    }

    private List<List<String>> getProductKeyListFromS3(String address) {
        if (address == null) {
            return null;
        }
        String[] tempArr = address.split("/", 2); //String[]{bucketName, key}
        S3Object s3Object = s3ItemDao.getItem(tempArr[0], tempArr[1]);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] buffer;
        try {
            buffer = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("getProductKeyListFromS3 exception", e);
        }
        List<List<String>> result = new ArrayList<>();
        int step = KEY_LENGTH + 1;
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < buffer.length; i += step) {
            String key = new String(buffer, i, KEY_LENGTH);
            keys.add(key);
            byte delimiter = buffer[i + KEY_LENGTH];
            if (delimiter == DELIMITER2[0]) {
                result.add(keys);
                keys = new ArrayList<>();
            }
        }
        return result;
    }

    private ProductKeyBatch toProductKeyBatch(ProductKeyBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(entity.getId());
        batch.setQuantity(entity.getQuantity());
        batch.setStatusCode(entity.getStatusCode());
        batch.setOrgId(entity.getOrgId());
        batch.setProductBaseId(entity.getProductBaseId());
        batch.setCreatedAppId(entity.getCreatedAppId());
        batch.setCreatedAccountId(entity.getCreatedAccountId());
        batch.setCreatedDateTime(entity.getCreatedDateTime());
        String codes = entity.getProductKeyTypeCodes();
        if (codes != null) {
            batch.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(codes, ",")));
        }
        batch.setProductKeysUri(entity.getProductKeysUri());
        return batch;
    }

    private ProductKeyBatchEntity toProductKeyBatchEntity(ProductKeyBatch batch) {
        if (batch == null) {
            return null;
        }
        ProductKeyBatchEntity entity = new ProductKeyBatchEntity();
        entity.setId(batch.getId());
        entity.setQuantity(batch.getQuantity());
        entity.setStatusCode(batch.getStatusCode());
        entity.setOrgId(batch.getOrgId());
        entity.setProductBaseId(batch.getProductBaseId());
        entity.setCreatedAppId(batch.getCreatedAppId());
        entity.setCreatedAccountId(batch.getCreatedAccountId());
        entity.setCreatedDateTime(batch.getCreatedDateTime());
        List<String> codes = batch.getProductKeyTypeCodes();
        if (codes != null) {
            entity.setProductKeyTypeCodes(StringUtils.collectionToDelimitedString(codes, ","));
        }
        entity.setProductKeysUri(batch.getProductKeysUri());
        return entity;
    }

    private static class ProductKeyBatchS3Object {

        private String batchId;
        private Integer quantity;
        private Long createdDateTime;
        private List<String> productKeyTypeCodes;
        private Long totalKeyLength;


        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Long getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(Long createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public List<String> getProductKeyTypeCodes() {
            return productKeyTypeCodes;
        }

        public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
            this.productKeyTypeCodes = productKeyTypeCodes;
        }

        public Long getTotalKeyLength() {
            return totalKeyLength;
        }

        public void setTotalKeyLength(Long totalKeyLength) {
            this.totalKeyLength = totalKeyLength;
        }
    }

}
