package com.yunsoo.key.service.impl;

import com.amazonaws.util.IOUtils;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.Constants;
import com.yunsoo.key.api.util.PageUtils;
import com.yunsoo.key.dao.entity.KeyBatchEntity;
import com.yunsoo.key.dao.repository.KeyBatchRepository;
import com.yunsoo.key.dto.KeyBatch;
import com.yunsoo.key.dto.KeyBatchCreationRequest;
import com.yunsoo.key.dto.Keys;
import com.yunsoo.key.file.service.FileService;
import com.yunsoo.key.processor.service.ProcessorService;
import com.yunsoo.key.service.KeyBatchService;
import com.yunsoo.key.service.KeyService;
import com.yunsoo.key.service.util.BatchNoGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private KeyService keyService;

    @Autowired
    private ProcessorService processorService;


    @Override
    public KeyBatch getById(String batchId) {
        if (batchId == null || batchId.length() == 0) {
            return null;
        }
        return toKeyBatch(keyBatchRepository.findOne(batchId));
    }

    @Override
    public Page<KeyBatch> getByFilter(String orgId, String productBaseId, List<String> statusCodeIn, String createdAccountId, DateTime createdDateTimeGE, DateTime createdDateTimeLE, Pageable pageable) {
        if (StringUtils.isEmpty(orgId)) {
            return Page.empty();
        }
        return PageUtils.convert(keyBatchRepository.query(
                orgId,
                productBaseId,
                statusCodeIn == null || statusCodeIn.size() == 0 ? null : statusCodeIn,
                statusCodeIn == null || statusCodeIn.size() == 0,
                createdAccountId,
                createdDateTimeGE,
                createdDateTimeLE,
                pageable)).map(this::toKeyBatch);
    }

    @Override
    public Keys getKeysById(String batchId) {
        KeyBatchEntity batchEntity = keyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            return null;
        }

        List<List<String>> keyList = getKeysFromFile(batchEntity.getOrgId(), batchId);

        if (keyList == null) {
            return null;
        }
        KeyBatch keyBatch = toKeyBatch(batchEntity);
        Keys keys = new Keys();
        keys.setKeyBatchId(batchEntity.getId());
        keys.setQuantity(batchEntity.getQuantity());
        keys.setCreatedDateTime(batchEntity.getCreatedDateTime());
        keys.setKeyTypeCodes(keyBatch.getKeyTypeCodes());
        keys.setKeys(keyList);
        return keys;
    }

    @Transactional
    @Override
    public KeyBatch create(KeyBatchCreationRequest request) {
        //region validation and pre-processing
        boolean hasExternalKeys = false;
        String partitionId = request.getPartitionId();
        int quantity = request.getQuantity();
        List<String> keyTypeCodes = request.getKeyTypeCodes();
        List<String> externalKeys = request.getExternalKeys();
        String productStatusCode = request.getProductStatusCode();
        if (keyTypeCodes == null || keyTypeCodes.size() == 0) {
            throw new BadRequestException("key_type_codes must not be null or empty");
        }
        if (Constants.KeyType.EXTERNAL.equals(keyTypeCodes.get(0))) {
            throw new BadRequestException("key_type_codes invalid, primary key can not be external");
        }

        for (String c : keyTypeCodes) {
            if (!Constants.KeyType.ALL.contains(c)) {
                throw new BadRequestException("key_type_codes invalid");
            }
            if (Constants.KeyType.EXTERNAL.equals(c)) {
                if (hasExternalKeys) {
                    throw new BadRequestException("key_type_codes invalid");
                }
                hasExternalKeys = true;
            }
        }
        if (hasExternalKeys) {
            if (externalKeys == null || externalKeys.size() != quantity) {
                throw new BadRequestException("external_keys invalid");
            }
            if (partitionId == null || partitionId.length() == 0) {
                partitionId = ObjectIdGenerator.getNew();
            }
        } else {
            externalKeys = null;
        }
        if (productStatusCode == null || productStatusCode.length() == 0) {
            productStatusCode = Constants.ProductStatus.ACTIVATED; // default activated
        } else if (!Constants.ProductStatus.ALL.contains(productStatusCode)) {
            throw new BadRequestException("product_status_code invalid");
        }
        //endregion

        //init batch
        KeyBatch batch = new KeyBatch();
        batch.setPartitionId(partitionId);
        batch.setBatchNo(request.getBatchNo() != null ? request.getBatchNo() : BatchNoGenerator.getNew());
        batch.setQuantity(quantity);
        batch.setStatusCode(Constants.KeyBatchStatus.CREATING);
        batch.setKeyTypeCodes(keyTypeCodes);
        batch.setProductStatusCode(productStatusCode);
        batch.setProductBaseId(request.getProductBaseId());
        batch.setOrgId(request.getOrgId());
        batch.setCreatedAppId(request.getCreatedAppId());
        batch.setCreatedDeviceId(request.getCreatedDeviceId());
        batch.setCreatedAccountId(request.getCreatedAccountId());
        batch.setCreatedDateTime(DateTime.now());

        //generate keys
        List<List<String>> keyList = hasExternalKeys
                ? generateProductKeys(quantity, keyTypeCodes, partitionId, externalKeys)
                : generateProductKeys(quantity, keyTypeCodes);

        //save batch
        KeyBatchEntity entity = keyBatchRepository.save(toKeyBatchEntity(batch));

        //save keys to file
        saveKeysToFile(entity.getOrgId(), entity.getId(), entity.getQuantity(), entity.getKeyTypeCodes(), keyList);

        //put to queue
        processorService.putKeyBatchCreateMessageToQueue(entity.getId());

        log.info(String.format("KeyBatch created [id: %s, quantity: %d]", entity.getId(), entity.getQuantity()));

        return toKeyBatch(entity);
    }

    @Transactional
    @Override
    public void patchUpdate(KeyBatch batch) {
        Assert.hasText(batch.getId(), "batchId must not be null or empty");

        KeyBatchEntity entity = keyBatchRepository.findOne(batch.getId());
        if (entity == null) {
            throw new NotFoundException("keyBatch not found by id: " + batch.getId());
        }

        if (batch.getBatchNo() != null) {
            entity.setBatchNo(batch.getBatchNo());
        }
        if (batch.getProductBaseId() != null) {
            entity.setProductBaseId(batch.getProductBaseId());
        }
        if (batch.getStatusCode() != null && Constants.KeyBatchStatus.ALL.contains(batch.getStatusCode())) {
            entity.setStatusCode(batch.getStatusCode());
        }

        keyBatchRepository.save(entity);
    }

    @Override
    public ResourceInputStream getKeyBatchDetails(String batchId) {
        KeyBatchEntity batchEntity = keyBatchRepository.findOne(batchId);
        if (batchEntity == null) {
            throw new NotFoundException("keyBatch not found by id: " + batchId);
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

    private List<List<String>> generateProductKeys(int quantity, List<String> keyTypeCodes) {
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

    private List<List<String>> generateProductKeys(int quantity, List<String> keyTypeCodes,
                                                   String partitionId, List<String> externalKeys) {
        int externalIndex = -1;
        for (int i = 0; i < keyTypeCodes.size(); i++) {
            if (Constants.KeyType.EXTERNAL.equals(keyTypeCodes.get(i))) {
                externalIndex = i;
            }
        }

        List<List<String>> keyList = new ArrayList<>(quantity);
        for (int i = 0, len = keyTypeCodes.size(); i < quantity; i++) {
            List<String> tempKeys = new ArrayList<>(len);
            for (int j = 0; j < len; j++) {
                if (j == externalIndex) {
                    tempKeys.add(keyService.formatExternalKey(partitionId, externalKeys.get(i)));
                } else {
                    tempKeys.add(KeyGenerator.getNew());
                }
            }
            keyList.add(tempKeys);
        }

        return keyList;
    }

    private void saveKeysToFile(String orgId, String batchId, int quantity, String keyTypeCodes, List<List<String>> keyList) {
        YSFile ysFile = new YSFile(YSFile.EXT_PKS);
        ysFile.putHeader("org_id", orgId);
        ysFile.putHeader("product_key_batch_id", batchId);
        ysFile.putHeader("quantity", Integer.toString(quantity));
        ysFile.putHeader("product_key_type_codes", keyTypeCodes);

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

    private List<List<String>> getKeysFromFile(String orgId, String batchId) {
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
            log.error(String.format("getKeysFromFile exception: [path: %s]", path), e);
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
            log.error(String.format("getKeysFromFile result size not equal to quantity: [path: %s]", path));
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
        batch.setPartitionId(entity.getPartitionId());
        batch.setBatchNo(entity.getBatchNo());
        batch.setQuantity(entity.getQuantity());
        batch.setStatusCode(entity.getStatusCode());
        batch.setKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getKeyTypeCodes())));
        batch.setProductStatusCode(entity.getProductStatusCode());
        batch.setProductBaseId(entity.getProductBaseId());
        batch.setOrgId(entity.getOrgId());
        batch.setCreatedAppId(entity.getCreatedAppId());
        batch.setCreatedDeviceId(entity.getCreatedDeviceId());
        batch.setCreatedAccountId(entity.getCreatedAccountId());
        batch.setCreatedDateTime(entity.getCreatedDateTime());
        return batch;
    }

    private KeyBatchEntity toKeyBatchEntity(KeyBatch batch) {
        if (batch == null) {
            return null;
        }
        KeyBatchEntity entity = new KeyBatchEntity();
        entity.setId(batch.getId());
        entity.setPartitionId(batch.getPartitionId());
        entity.setBatchNo(batch.getBatchNo());
        entity.setQuantity(batch.getQuantity());
        entity.setStatusCode(batch.getStatusCode());
        entity.setKeyTypeCodes(StringUtils.collectionToCommaDelimitedString(batch.getKeyTypeCodes()));
        entity.setProductStatusCode(batch.getProductStatusCode());
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
