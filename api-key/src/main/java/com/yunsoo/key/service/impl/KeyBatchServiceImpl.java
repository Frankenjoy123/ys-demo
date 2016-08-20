package com.yunsoo.key.service.impl;

import com.amazonaws.util.IOUtils;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dao.entity.KeyBatchEntity;
import com.yunsoo.key.dao.repository.KeyBatchRepository;
import com.yunsoo.key.dto.KeyBatch;
import com.yunsoo.key.dto.Keys;
import com.yunsoo.key.file.service.FileService;
import com.yunsoo.key.service.KeyBatchService;
import com.yunsoo.key.service.util.BatchNoGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-19
 * Descriptions:
 */
@Service
public class KeyBatchServiceImpl implements KeyBatchService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyBatchRepository keyBatchRepository;

    @Autowired
    private FileService fileService;


    @Override
    public KeyBatch getById(String batchId) {
        if (batchId == null || batchId.length() == 0) {
            return null;
        }
        return toKeyBatch(keyBatchRepository.findOne(batchId));
    }

    @Override
    public Keys getKeysById(String batchId) {
        KeyBatchEntity batchEntity = keyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            return null;
        }

        List<List<String>> keyList = getProductKeyListFromFile(batchEntity.getOrgId(), batchId);

        if (keyList == null) {
            return null;
        }
        KeyBatch keyBatch = toKeyBatch(batchEntity);
        Keys keys = new Keys();
        keys.setBatchId(batchEntity.getId());
        keys.setQuantity(batchEntity.getQuantity());
        keys.setCreatedDateTime(batchEntity.getCreatedDateTime());
        keys.setKeyTypeCodes(keyBatch.getKeyTypeCodes());
        keys.setKeys(keyList);
        return keys;
    }

    @Override
    public KeyBatch create(KeyBatch batch) {
        int quantity = batch.getQuantity();
        List<String> keyTypeCodes = batch.getKeyTypeCodes();

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
        KeyBatchEntity newEntity = keyBatchRepository.save(toProductKeyBatchEntity(batch));

        //save product keys to S3
        saveProductKeyListToFile(
                newEntity.getOrgId(),
                newEntity.getId(),
                newEntity.getQuantity(),
                newEntity.getKeyTypeCodes(),
                keyList);

        return toKeyBatch(newEntity);
    }

    @Override
    public void patchUpdate(KeyBatch batch) {
        Assert.hasText(batch.getId(), "batchId must not be null or empty");

        KeyBatchEntity entity = keyBatchRepository.findOne(batch.getId());
        if (entity == null) {
            throw new NotFoundException("keyBatch not found by id: " + batch.getId());
        }

        if (batch.getProductBaseId() != null) {
            entity.setProductBaseId(batch.getProductBaseId());
        }
        if (batch.getStatusCode() != null) {
            entity.setStatusCode(batch.getStatusCode());
        }

        keyBatchRepository.save(entity);
    }

    @Override
    public ResourceInputStream getKeyBatchDetails(String batchId) {
        KeyBatchEntity batchEntity = keyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            return null;
        }
        String path = formatKeyBatchDetailsFilePath(batchEntity.getOrgId(), batchId);
        return fileService.getFile(path);
    }

    @Override
    public void saveKeyBatchDetails(String batchId, ResourceInputStream details) {
        KeyBatchEntity batchEntity = keyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            throw new NotFoundException("keyBatch not found by id: " + batchId);
        }
        String path = formatKeyBatchDetailsFilePath(batchEntity.getOrgId(), batchId);
        fileService.putFile(path, details);
    }


    //region private methods

    private List<List<String>> generateProductKeys(KeyBatch batch) {
        int quantity = batch.getQuantity();
        List<String> keyTypeCodes = batch.getKeyTypeCodes();

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

    private void saveProductKeyListToFile(String orgId,
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

        String path = formatKeyBatchKeysFilePath(orgId, batchId);
        InputStream inputStream = new ByteArrayInputStream(data);
        fileService.putFile(path, new ResourceInputStream(inputStream, data.length, "application/vnd+ys.pks"));
    }

    private List<List<String>> getProductKeyListFromFile(String orgId, String batchId) {
        String path = formatKeyBatchKeysFilePath(orgId, batchId);
        ResourceInputStream resourceInputStream = fileService.getFile(path);
        if (resourceInputStream == null) {
            return null;
        }
        YSFile ysFile;
        try {
            byte[] buffer = IOUtils.toByteArray(resourceInputStream);
            ysFile = YSFile.read(buffer);
        } catch (Exception e) {
            log.error(String.format("getProductKeyListFromFile exception: [path: %s]", path), e);
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
            log.error(String.format("getProductKeyListFromFile result size not equal to quantity: [path: %s]", path));
            return null;
        }
    }


    private String formatKeyBatchKeysFilePath(String orgId, String batchId) {
        return String.format("organization/%s/product_key_batch/%s/keys.pks", orgId, batchId);
    }

    private String formatKeyBatchDetailsFilePath(String orgId, String batchId) {
        return String.format("organization/%s/product_key_batch/%s/details.json", orgId, batchId);
    }

    private KeyBatch toKeyBatch(KeyBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        KeyBatch batch = new KeyBatch();
        batch.setId(entity.getId());
        batch.setBatchNo(entity.getBatchNo());
        batch.setQuantity(entity.getQuantity());
        batch.setStatusCode(entity.getStatusCode());
        batch.setKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getKeyTypeCodes())));
        batch.setProductBaseId(entity.getProductBaseId());
        batch.setOrgId(entity.getOrgId());
        batch.setCreatedAppId(entity.getCreatedAppId());
        batch.setCreatedDeviceId(entity.getCreatedDeviceId());
        batch.setCreatedAccountId(entity.getCreatedAccountId());
        batch.setCreatedDateTime(entity.getCreatedDateTime());
        return batch;
    }

    private KeyBatchEntity toProductKeyBatchEntity(KeyBatch batch) {
        if (batch == null) {
            return null;
        }
        KeyBatchEntity entity = new KeyBatchEntity();
        entity.setId(batch.getId());
        entity.setBatchNo(batch.getBatchNo());
        entity.setQuantity(batch.getQuantity());
        entity.setStatusCode(batch.getStatusCode());
        entity.setKeyTypeCodes(StringUtils.collectionToCommaDelimitedString(batch.getKeyTypeCodes()));
        entity.setProductBaseId(batch.getProductBaseId());
        entity.setOrgId(batch.getOrgId());
        entity.setCreatedAppId(batch.getCreatedAppId());
        entity.setCreatedDeviceId(batch.getCreatedDeviceId());
        entity.setCreatedAccountId(batch.getCreatedAccountId());
        entity.setCreatedDateTime(batch.getCreatedDateTime());
        return entity;
    }

    //endregion

}
