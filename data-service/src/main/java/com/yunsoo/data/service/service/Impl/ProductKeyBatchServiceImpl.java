package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.data.service.config.AWSProperties;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import com.yunsoo.data.service.service.FileService;
import com.yunsoo.data.service.service.ProductKeyBatchService;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;
import com.yunsoo.data.service.service.exception.ServiceException;
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
    private AWSProperties awsProperties;

    @Autowired
    private FileService fileService;


    @Override
    public ProductKeyBatch getById(String batchId) {
        return toProductKeyBatch(productKeyBatchRepository.findOne(batchId));
    }

    @Override
    public ProductKeys getProductKeysByBatchId(String batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);
        if (keyBatch == null) {
            return null;
        }
        List<List<String>> keys = getProductKeyListFromS3(keyBatch.getOrgId(), batchId, keyBatch.getProductKeysUri());
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
    public S3Object getProductKeyBatchDetails(String batchId) {
        ProductKeyBatchEntity batchEntity = productKeyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            return null;
        }
        String bucketName = awsProperties.getS3().getBucketName();
        String s3Key = formatProductKeyBatchDetailsS3Key(batchEntity.getOrgId(), batchId);
        return fileService.getFile(bucketName, s3Key);
    }

    @Override
    public void saveProductKeyBatchDetails(String batchId, InputStream inputStream, String contentType, long contentLength) {
        ProductKeyBatchEntity batchEntity = productKeyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            throw ServiceException.notFound("productKeyBatch not found by [id: " + batchId + "]");
        }
        String bucketName = awsProperties.getS3().getBucketName();
        String s3Key = formatProductKeyBatchDetailsS3Key(batchEntity.getOrgId(), batchId);
        ObjectMetadata metadata = new ObjectMetadata();
        if (contentType != null) {
            metadata.setContentType(contentType);
        }
        if (contentLength > 0) {
            metadata.setContentLength(contentLength);
        }
        s3ItemDao.putItem(bucketName, s3Key, inputStream, metadata);
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
        ProductKeyBatchEntity entity = toProductKeyBatchEntity(batch);
        productKeyBatchRepository.save(entity);

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

        String bucketName = awsProperties.getS3().getBucketName();
        String s3Key = formatProductKeyBatchKeysS3Key(orgId, batchId);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(totalKeyLength);
        metadata.setContentType("application/vnd+yunsoo.pks");
        s3ItemDao.putItem(bucketName, s3Key, inputStream, metadata);

        return String.join("/", bucketName, s3Key); //uri
    }

    private List<List<String>> getProductKeyListFromS3(String orgId, String batchId, String uri) {
        String bucketName = awsProperties.getS3().getBucketName();
        String s3Key;
        if (uri == null) {
            s3Key = formatProductKeyBatchKeysS3Key(orgId, batchId);
        } else {
            String[] tempArr = uri.split("/", 2); //String[]{bucketName, key}
            s3Key = tempArr[1];
        }
        S3Object s3Object = s3ItemDao.getItem(bucketName, s3Key);
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

    private String formatProductKeyBatchKeysS3Key(String orgId, String batchId) {
        return String.format("organization/%s/product_key_batch/%s/keys.pks", orgId, batchId);
    }

    private String formatProductKeyBatchDetailsS3Key(String orgId, String batchId) {
        return String.format("organization/%s/product_key_batch/%s/details.json", orgId, batchId);
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
        batch.setRestQuantity(entity.getRestQuantity());
        String codes = entity.getProductKeyTypeCodes();
        if (codes != null) {
            batch.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(codes, ",")));
        }
        batch.setProductKeysUri(entity.getProductKeysUri());
        batch.setMarketingId(entity.getMarketingId());
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
        entity.setRestQuantity(batch.getRestQuantity());
        List<String> codes = batch.getProductKeyTypeCodes();
        if (codes != null) {
            entity.setProductKeyTypeCodes(StringUtils.collectionToDelimitedString(codes, ","));
        }
        entity.setProductKeysUri(batch.getProductKeysUri());
        entity.setMarketingId(batch.getMarketingId());
        return entity;
    }

}
