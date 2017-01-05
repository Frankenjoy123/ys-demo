package com.yunsoo.key.api.controller;

import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.SerialNoGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.Constants;
import com.yunsoo.key.api.util.PageUtils;
import com.yunsoo.key.api.util.ResponseEntityUtils;
import com.yunsoo.key.dto.KeyBatch;
import com.yunsoo.key.dto.KeyBatchCreationRequest;
import com.yunsoo.key.dto.Keys;
import com.yunsoo.key.service.KeyBatchService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
@RestController
@RequestMapping("/keyBatch")
public class KeyBatchController {

    @Autowired
    private KeyBatchService keyBatchService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public KeyBatch getById(@PathVariable(value = "id") String id) {
        KeyBatch keyBatch = keyBatchService.getById(id);
        if (keyBatch == null) {
            throw new NotFoundException("keyBatch not found by id: " + id);
        }
        return keyBatch;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<KeyBatch> getByFilterPaged(@RequestParam(value = "org_id", required = false) String orgId,
                                           @RequestParam(value = "org_ids", required = false) List<String> orgIdIn,
                                           @RequestParam(value = "downloaded", required = false) Boolean downloaded,
                                           @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                           @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
                                           @RequestParam(value = "created_account_id", required = false) String createdAccountId,
                                           @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
                                           @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
                                           Pageable pageable,
                                           HttpServletResponse response) {
        if(orgIdIn == null){
            orgIdIn = new ArrayList<>();
            orgIdIn.add(orgId);
        }
        Page<KeyBatch> page = keyBatchService.getByFilter(orgIdIn, productBaseId, statusCodeIn, createdAccountId, downloaded == null?false: downloaded, createdDateTimeGE, createdDateTimeLE, pageable);
        return PageUtils.response(response, page, page != null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public KeyBatch create(@RequestBody @Valid KeyBatchCreationRequest creationRequest) {
        validateSerialNoPattern(creationRequest.getSerialNoPattern(), creationRequest.getQuantity());
        return keyBatchService.create(creationRequest);
    }

    @RequestMapping(value = "file", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public KeyBatch createFromFile(HttpServletRequest request) throws IOException {
        YSFile ysFile;
        try {
            ysFile = YSFile.read(request.getInputStream());
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
        if (!YSFile.EXT_PKS.equals(ysFile.getEXT())) {
            throw new BadRequestException("file EXT invalid");
        }
        KeyBatchCreationRequest creationRequest = toKeyBatchCreationRequest(ysFile);
        validateSerialNoPattern(creationRequest.getSerialNoPattern(), creationRequest.getQuantity());
        return keyBatchService.create(creationRequest);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody KeyBatch batch) {
        batch.setId(id);
        keyBatchService.patchUpdate(batch);
    }

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    public Keys getKeys(@PathVariable(value = "id") String id) {
        Keys keys = keyBatchService.getKeysById(id);
        if (keys == null) {
            throw new NotFoundException("keys not found by batchId: " + id);
        }
        return keys;
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductKeyBatchDetails(@PathVariable(value = "id") String id) throws IOException {
        ResourceInputStream details = keyBatchService.getKeyBatchDetails(id);
        if (details == null) {
            return null;
        }
        return ResponseEntityUtils.convert(details);
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.PUT)
    public void putProductKeyBatchDetails(@PathVariable(value = "id") String id, HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        long contentLength = request.getContentLengthLong();
        ServletInputStream inputStream = request.getInputStream();
        keyBatchService.saveKeyBatchDetails(id, new ResourceInputStream(inputStream, contentLength, contentType));
    }

    private void validateSerialNoPattern(String serialNoPattern, int quantity) {
        if (StringUtils.isEmpty(serialNoPattern)) {
            return;
        }
        try {
            SerialNoGenerator serialNoGenerator = new SerialNoGenerator(serialNoPattern);
            if (serialNoGenerator.getTotalCount() != quantity) {
                throw new BadRequestException("serial_no_pattern invalid");
            }
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("serial_no_pattern invalid");
        }
    }

    private KeyBatchCreationRequest toKeyBatchCreationRequest(YSFile ysFile) {
        String partitionId = ysFile.getHeader("partition_id");
        String batchNo = ysFile.getHeader("batch_no");
        String quantity = ysFile.getHeader("quantity");
        String keyTypeCodes = ysFile.getHeader("key_type_codes");
        String productBaseId = ysFile.getHeader("product_base_id");
        String productStatusCode = ysFile.getHeader("product_status_code");
        String orgId = ysFile.getHeader("org_id");
        String serialNoPattern = ysFile.getHeader("serial_no_pattern");
        String createdAppId = ysFile.getHeader("created_app_id");
        String createdDeviceId = ysFile.getHeader("created_device_id");
        String createdAccountId = ysFile.getHeader("created_account_id");

        int quantityInt;
        List<String> keyTypeCodeList;
        List<String> externalKeys = null;

        if (StringUtils.isEmpty(quantity)
                || StringUtils.isEmpty(keyTypeCodes)
                || StringUtils.isEmpty(orgId)) {
            throw new BadRequestException("required fields are missing from file header");
        }
        try {
            quantityInt = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new BadRequestException("quantity invalid");
        }
        keyTypeCodeList = Arrays.asList(StringUtils.commaDelimitedListToStringArray(keyTypeCodes));
        if (keyTypeCodeList.contains(Constants.KeyType.EXTERNAL)) {
            String contentStr = new String(ysFile.getContent(), StandardCharsets.UTF_8);
            externalKeys = Arrays.asList(contentStr.split("\r\n"))
                    .stream()
                    .filter(k -> k != null)
                    .map(String::trim)
                    .filter(k -> k.length() > 0)
                    .collect(Collectors.toList());
            if (quantityInt <= 0 || quantityInt != externalKeys.size()) {
                throw new BadRequestException("quantity invalid");
            }
        }

        KeyBatchCreationRequest creationRequest = new KeyBatchCreationRequest();
        creationRequest.setPartitionId(partitionId);
        creationRequest.setBatchNo(batchNo);
        creationRequest.setQuantity(quantityInt);
        creationRequest.setKeyTypeCodes(keyTypeCodeList);
        creationRequest.setProductBaseId(productBaseId);
        creationRequest.setProductStatusCode(productStatusCode);
        creationRequest.setOrgId(orgId);
        creationRequest.setSerialNoPattern(serialNoPattern);
        creationRequest.setCreatedAppId(createdAppId);
        creationRequest.setCreatedDeviceId(createdDeviceId);
        creationRequest.setCreatedAccountId(createdAccountId);
        creationRequest.setExternalKeys(externalKeys);
        return creationRequest;
    }

}
