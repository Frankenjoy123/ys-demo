package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import com.yunsoo.data.service.service.ProductKeyBatchService;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.data.service.dbmodel.ProductKeyBatchS3ObjectModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Service("productKeyBatchService")
public class ProductKeyBatchServiceImpl implements ProductKeyBatchService {

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;

    @Autowired
    private S3ItemDao s3ItemDao;

    @Autowired
    private AmazonSetting amazonSetting;


    @Override
    public ProductKeyBatch getById(String id) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(id);
        return fromProductKeyBatchEntity(entity);
    }

    @Override
    public List<ProductKeyBatch> getByOrganizationIdPaged(String orgId, int pageIndex, int pageSize) {
        Page<ProductKeyBatchEntity> entityPage =
                productKeyBatchRepository.findByOrgIdOrderByCreatedDateTimeDesc(orgId, new PageRequest(pageIndex, pageSize));
        return StreamSupport.stream(entityPage.spliterator(), false)
                .map(this::fromProductKeyBatchEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductKeyBatch> getByFilterPaged(String orgId, String productBaseId, int pageIndex, int pageSize) {
        Page<ProductKeyBatchEntity> entityPage =
                productKeyBatchRepository.findByOrgIdAndProductBaseIdOrderByCreatedDateTimeDesc(orgId, productBaseId, new PageRequest(pageIndex, pageSize));
        return StreamSupport.stream(entityPage.spliterator(), false)
                .map(this::fromProductKeyBatchEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ProductKeys getProductKeysByBatchId(String batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);
        if (keyBatch == null) {
            return null;
        }
        return getProductKeysByUri(keyBatch.getProductKeysUri());
    }

    @Override
    public ProductKeys getProductKeysByUri(String uri) {
        Assert.notNull(uri, "uri must not be null");
        ProductKeyBatchS3ObjectModel model = getProductKeyListFromS3(uri);
        if (model == null) {
            return null;
        }
        ProductKeys productKeys = new ProductKeys();
        productKeys.setBatchId(model.getId());
        productKeys.setQuantity(model.getQuantity());
        productKeys.setCreatedDateTime(DateTimeUtils.parse(model.getCreatedDateTime()));
        productKeys.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(model.getProductKeyTypeCodes(), ",")));
        productKeys.setProductKeys(model.getProductKeys());
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
                newEntity.getId(),
                newEntity.getQuantity(),
                newEntity.getCreatedDateTime(),
                newEntity.getProductKeyTypeCodes(),
                keyList);

        //update batch with uri
        newEntity.setProductKeysUri(uri);
        newEntity = productKeyBatchRepository.save(newEntity);

        return fromProductKeyBatchEntity(newEntity);
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

    private String saveProductKeyListToS3(String batchId,
                                          Integer quantity,
                                          DateTime createdDateTime,
                                          String productKeyTypeCodes,
                                          List<List<String>> keyList) {
        ProductKeyBatchS3ObjectModel model = new ProductKeyBatchS3ObjectModel();
        model.setId(batchId);
        model.setQuantity(quantity);
        model.setCreatedDateTime(DateTimeUtils.toString(createdDateTime));
        model.setProductKeyTypeCodes(productKeyTypeCodes);
        model.setProductKeys(keyList);
        String bucketName = amazonSetting.getS3_basebucket();
        String key = String.join("/", amazonSetting.getS3_product_key_batch_path(), batchId);
        s3ItemDao.putItem(bucketName, key, model);
        return String.join("/", bucketName, key); //uri
    }

    private ProductKeyBatchS3ObjectModel getProductKeyListFromS3(String address) {
        if (address == null) {
            return null;
        }
        String[] tempArr = address.split("/", 2); //String[]{bucketName, key}
        //ProductKeyBatchS3ObjectModel
        return s3ItemDao.getItem(
                tempArr[0],
                tempArr[1],
                ProductKeyBatchS3ObjectModel.class);
    }

    private ProductKeyBatch fromProductKeyBatchEntity(ProductKeyBatchEntity entity) {
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

    public static ProductKeyBatchEntity toProductKeyBatchEntity(ProductKeyBatch batch) {
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

}
