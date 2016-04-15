package com.yunsoo.data.service.service.Impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.support.YSFile;
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
import com.yunsoo.data.service.util.BatchNoGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
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
    private static final byte[] DELIMITER2 = new byte[]{0x3B}; //;

    private Log log = LogFactory.getLog(this.getClass());

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
        ProductKeyBatchEntity batchEntity = productKeyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            return null;
        }

        List<List<String>> keys;
        //move old file location to keys.pks
        if (batchEntity.getProductKeysUri() != null && batchEntity.getProductKeysUri().length() > 0) {
            String bucketName = awsProperties.getS3().getBucketName();
            String s3Key = batchEntity.getProductKeysUri().split("/", 2)[1];
            keys = getProductKeyListFromS3Old(bucketName, s3Key);
            if (keys != null) {
                saveProductKeyListToS3(batchEntity.getOrgId(), batchId, batchEntity.getQuantity(), batchEntity.getProductKeyTypeCodes(), keys);
                batchEntity.setProductKeysUri(null);
                productKeyBatchRepository.save(batchEntity);
                s3ItemDao.deleteItem(bucketName, s3Key);
            }
        } else {
            keys = getProductKeyListFromS3(batchEntity.getOrgId(), batchId);
        }

        if (keys == null) {
            return null;
        }
        ProductKeyBatch keyBatch = toProductKeyBatch(batchEntity);
        ProductKeys productKeys = new ProductKeys();
        productKeys.setBatchId(batchEntity.getId());
        productKeys.setQuantity(batchEntity.getQuantity());
        productKeys.setCreatedDateTime(batchEntity.getCreatedDateTime());
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
        batch.setBatchNo(BatchNoGenerator.getNew());
        ProductKeyBatchEntity newEntity = productKeyBatchRepository.save(toProductKeyBatchEntity(batch));

        //save product keys to S3
        saveProductKeyListToS3(
                newEntity.getOrgId(),
                newEntity.getId(),
                newEntity.getQuantity(),
                newEntity.getProductKeyTypeCodes(),
                keyList);

        return toProductKeyBatch(newEntity);
    }

    @Override
    public void patchUpdate(ProductKeyBatch batch) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(batch.getId());
        if (entity == null) {
            throw ServiceException.notFound("productKeyBatch not found by id: " + batch.getId());
        }
        if (batch.getProductBaseId() != null) {
            entity.setProductBaseId(batch.getProductBaseId());
        }
        if (batch.getStatusCode() != null) {
            entity.setStatusCode(batch.getStatusCode());
        }
        if (batch.getRestQuantity() != null) {
            entity.setRestQuantity(batch.getRestQuantity());
        }
        if (batch.getMarketingId() != null) {
            entity.setMarketingId(batch.getMarketingId());
        }

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

    private void saveProductKeyListToS3(String orgId,
                                        String batchId,
                                        int quantity,
                                        String productKeyTypeCodes,
                                        List<List<String>> keyList) {
        YSFile ysFile = new YSFile(YSFile.EXT_PKS);
        ysFile.putHeader("org_id", orgId);
        ysFile.putHeader("product_key_batch_id", batchId);
        ysFile.putHeader("quantity", Integer.toString(quantity));
        ysFile.putHeader("product_key_type_codes", productKeyTypeCodes);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < quantity; i++) {
            List<String> keys = keyList.get(i);
            sb.append(StringUtils.collectionToCommaDelimitedString(keys));
            if (i < quantity - 1) {
                sb.append("\r\n");
            }
        }
        ysFile.setContent(sb.toString().getBytes(StandardCharsets.UTF_8));

        byte[] data = ysFile.toBytes();

        String bucketName = awsProperties.getS3().getBucketName();
        String s3Key = formatProductKeyBatchKeysS3Key(orgId, batchId);
        InputStream inputStream = new ByteArrayInputStream(data);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.length);
        metadata.setContentType("application/vnd+ys.pks");
        s3ItemDao.putItem(bucketName, s3Key, inputStream, metadata);
    }

    private List<List<String>> getProductKeyListFromS3Old(String bucketName, String s3Key) {
        S3Object s3Object = fileService.getFile(bucketName, s3Key);
        if (s3Object == null) {
            return null;
        }
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] buffer;
        try {
            buffer = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("getProductKeyListFromS3Old exception", e);
            return null;
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

    private List<List<String>> getProductKeyListFromS3(String orgId, String batchId) {
        String bucketName = awsProperties.getS3().getBucketName();
        String s3Key = formatProductKeyBatchKeysS3Key(orgId, batchId);
        S3Object s3Object = fileService.getFile(bucketName, s3Key);
        if (s3Object == null) {
            return null;
        }
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        YSFile ysFile;
        try {
            byte[] buffer = IOUtils.toByteArray(inputStream);
            ysFile = YSFile.read(buffer);
        } catch (Exception e) {
            log.error(String.format("getProductKeyListFromS3 exception: [s3Key: %s]", s3Key), e);
            return null;
        }
        byte[] content = ysFile.getContent();
        List<List<String>> result = new ArrayList<>();
        String contentStr = new String(content, StandardCharsets.UTF_8);
        String[] lines = contentStr.split("\r\n");
        for (String line : lines) {
            if (line.length() > 0) {
                result.add(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
            }
        }
        String quantity = ysFile.getHeader("quantity");
        if (quantity != null && Integer.parseInt(quantity) == result.size()) {
            return result;
        } else {
            log.error(String.format("getProductKeyListFromS3 result size not equal to quantity: [s3Key: %s]", s3Key));
            return null;
        }
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
        batch.setBatchNo(entity.getBatchNo());
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
            batch.setProductKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(codes)));
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
        entity.setBatchNo(batch.getBatchNo());
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
            entity.setProductKeyTypeCodes(StringUtils.collectionToCommaDelimitedString(codes));
        }
        entity.setProductKeysUri(batch.getProductKeysUri());
        entity.setMarketingId(batch.getMarketingId());
        return entity;
    }

}
