package com.yunsoo.api.domain;

import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.api.key.dto.KeyBatch;
import com.yunsoo.api.key.dto.KeyBatchCreationRequest;
import com.yunsoo.api.key.dto.KeySerialNo;
import com.yunsoo.api.key.dto.Keys;
import com.yunsoo.api.key.service.KeyBatchService;
import com.yunsoo.api.key.service.KeySerialNoService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
@Component
public class ProductKeyDomain {

    private static final byte[] CR_LF = new byte[]{13, 10};

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private FileService fileService;

    @Autowired
    private KeyBatchService keyBatchService;

    @Autowired
    private KeySerialNoService keySerialNoService;

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;

    @Autowired
    private ProductKeyTransactionDomain productKeyTransactionDomain;

    @Autowired
    private OrganizationConfigDomain orgConfigDomain;

    @Autowired
    private AuthOrganizationService authOrganizationService;

    @Value("${yunsoo.product_key_base_url}")
    private String productKeyBaseUrl;



    //ProductKeyBatch
    public String getKeyBatchPartitionId(String orgId) {
        List<String> partitionIds = dataApiClient.get("productkeybatch/partition?org_id={orgId}", new ParameterizedTypeReference<List<String>>() {
        }, orgId);

        return partitionIds.get(0);
    }

    public ProductKeyBatch getProductKeyBatchById(String id) {
        return toProductKeyBatch(
                dataApiClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id),
                lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType),
                lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus));
    }

    public ProductKeyBatchObject getProductKeyBatchObjectById(String id) {
        try {
            return dataApiClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }


    public Page<ProductKeyBatch> getProductKeyBatchesByFilterPaged(String orgId, List<String> orgIdIn, String productBaseId, Boolean isPackage, String createAccount, String deviceId, org.joda.time.LocalDate createdDateTimeStart, org.joda.time.LocalDate createdDateTimeEnd, Pageable pageable) {
        String[] statusCodes = new String[]{
                LookupCodes.ProductKeyBatchStatus.CREATING,
                LookupCodes.ProductKeyBatchStatus.AVAILABLE
        };
        QueryStringBuilder query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK);
        if (isPackage != null)
            query = query.append("org_id", orgId)
                    .append("is_package", isPackage)
                    .append("status_code_in", statusCodes)
                    .append("product_base_id", productBaseId)
                    .append("create_account", createAccount)
                    .append("device_id", deviceId)
                    .append("create_datetime_start", createdDateTimeStart)
                    .append("create_datetime_end", createdDateTimeEnd)
                    .append(pageable);
        else
            query = query.append("org_id", orgId).append("org_ids", orgIdIn)
                    .append("product_base_id", productBaseId)
                    .append("status_code_in", statusCodes)
                    .append(pageable);
        Page<ProductKeyBatchObject> objectsPage = dataApiClient.getPaged("productkeybatch" + query.build(), new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        });

        List<Lookup> productKeyTypes = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        List<Lookup> productKeyBatchStatuses = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus);
        return objectsPage.map(i -> toProductKeyBatch(i, productKeyTypes, productKeyBatchStatuses));
    }

    public Page<ProductKeyBatch> search(List<String> orgIdIn, Boolean downloaded, String productBaseId, String createdAccountId, DateTime start, DateTime end, Pageable pageable){
        List<String> statusCodeIn = Arrays.asList(LookupCodes.ProductKeyBatchStatus.CREATING, LookupCodes.ProductKeyBatchStatus.AVAILABLE);
        Page<KeyBatch> productKeyBatchPage = keyBatchService.getByFilter(orgIdIn, downloaded, productBaseId,statusCodeIn , createdAccountId, start, end, pageable);
        List<Lookup> productKeyTypes = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        List<Lookup> productKeyBatchStatuses = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus);
        return productKeyBatchPage.map(i -> toProductKeyBatch(i, productKeyTypes, productKeyBatchStatuses));

    }


    public Long sumQuantity(String orgId, String productBaseId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .build();
        return dataApiClient.get("productkeybatch/sum/quantity" + query, Long.class);
    }

    public Long sumTime(String orgId, String productBaseId, DateTime startTime, DateTime endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("start_time", startTime)
                .append("end_time", endTime)
                .build();
        return dataApiClient.get("productkeybatch/sum/time" + query, Long.class);
    }

    public ProductKeyBatch createProductKeyBatch(KeyBatchCreationRequest request) {
        request.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        String productBaseId = request.getProductBaseId();

        //check product key credit
        List<ProductKeyCredit> credits = productKeyOrderDomain.getProductKeyCredits(request.getOrgId(), productBaseId == null ? "*" : productBaseId);
        Long remain = 0L;
        for (ProductKeyCredit c : credits) {
            if (c.getProductBaseId() == null || c.getProductBaseId().equals(productBaseId)) {
                remain += c.getRemain();
            }
        }
        if (remain < request.getQuantity()) {
            log.warn("insufficient ProductKeyCredit");
            throw new UnprocessableEntityException("产品码可用额度不足");
        }

        //create new product key batch
        KeyBatch keyBatch = keyBatchService.create(request);
        log.info(String.format("ProductKeyBatch created [id: %s, statusCode: %s]", keyBatch.getId(), keyBatch.getStatusCode()));

        //purchase
        String transactionId = productKeyTransactionDomain.purchase(keyBatch);
        log.info(String.format("ProductKeyBatch purchased [transactionId: %s]", transactionId));

        //commit transaction
        productKeyTransactionDomain.commit(transactionId);
        log.info(String.format("ProductKeyTransaction committed [transactionId: %s]", transactionId));

        return toProductKeyBatch(keyBatch, lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType), lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus));
    }

    public void patchUpdateProductKeyBatch(ProductKeyBatchObject batchObj) {
        dataApiClient.patch("productkeybatch/{id}", batchObj, batchObj.getId());
    }

    public void putMarketingId(String id, String marketingId) {
        dataApiClient.put("productkeybatch/{id}/marketing_id", marketingId, id);
    }

    public List<ProductKeyBatchObject> getProductKeyBatchByMarketingId(String marketingId) {
        return dataApiClient.get("productkeybatch/marketing/{id}", new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        }, marketingId);
    }


    public byte[] getProductKeysByBatchId(String id, String orgId, String batchNo) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Keys productKeys = keyBatchService.getKeysByKeyBatchId(id);
        if (productKeys == null) {
            return null;
        }
        KeySerialNo currentKeySerialNo = null;
        List<KeySerialNo> serialNoList = keySerialNoService.getByFilter(orgId);
        if (serialNoList.size() > 0)
            currentKeySerialNo = serialNoList.get(0);

        try {
            List<String> productKeyTypeCodes = productKeys.getKeyTypeCodes();
            for (List<String> ks : productKeys.getKeys()) {
                for (int j = 0; j < ks.size(); j++) {
                    String value = productKeyBaseUrl + ks.get(j);
                    if (LookupCodes.ProductKeyType.EXTERNAL.equals(productKeyTypeCodes.get(j))) {
                        value = productKeyBaseUrl + "external/" + ks.get(j);
                    }
                    if (currentKeySerialNo != null) {
                        value += ", " + currentKeySerialNo.getPrefix() + "." + addZeroPrefix(String.valueOf(currentKeySerialNo.getOffset() + j + 1), currentKeySerialNo.getSerialLength()) + (currentKeySerialNo.getSuffix() == null ? "" : ("." + currentKeySerialNo.getSuffix()))
                                + ", " + batchNo;
                    }

                    ks.set(j, value);
                }
                outputStream.write(
                        StringUtils.collectionToDelimitedString(ks, ",")
                                .getBytes(Charset.forName("UTF-8")));
                outputStream.write(CR_LF);

                if (currentKeySerialNo != null) {  //update the offset number
                    currentKeySerialNo.setOffset(currentKeySerialNo.getOffset() + ks.size());
                    keySerialNoService.update(currentKeySerialNo);
                }

            }
        } catch (IOException e) {
            throw new InternalServerErrorException("product keys format issue");
        }
        return outputStream.toByteArray();
    }

    public int getProductKeysZipByBatchId(String id, ZipOutputStream out) {
        ProductKeyBatch batch = getProductKeyBatchById(id);
        if (batch == null) {
            throw new NotFoundException("product key batch not found " + StringFormatter.formatMap("id", id));
        }
        AuthUtils.checkPermission(batch.getOrgId(), "product_key_batch", "read");

        Map<String, Object> configMap = orgConfigDomain.getConfig(batch.getOrgId(), false, null);
        String downloadFileFormat = configMap.get("enterprise.product_key.format").toString();
        Organization org = authOrganizationService.getById(batch.getOrgId());
        String fileName = org.getName() + "_" + batch.getBatchNo() + "." + downloadFileFormat;
        byte[] data = getProductKeysByBatchId(id, batch.getOrgId(), batch.getBatchNo());

        try {
            out.putNextEntry(new ZipEntry(fileName));
            out.write(data);
            out.flush();
            out.closeEntry();

            ProductKeyBatchObject productKeyBatchObject = new ProductKeyBatchObject();
            productKeyBatchObject.setDownloadNo(batch.getDownloadNo() + 1);
            productKeyBatchObject.setId(batch.getId());
            patchUpdateProductKeyBatch(productKeyBatchObject);

            return data.length;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("add file error when download zip file: " + fileName);
        }

        return 0;
    }

    public YSFile getProductKeyFile(String batchId, String orgId) {
        String path = String.format("organization/%s/product_key_batch/%s/keys.pks", orgId, batchId);
        ResourceInputStream resourceInputStream = fileService.getFile(path);
        if (resourceInputStream == null) {
            return null;
        }
        try {
            YSFile ysFile = YSFile.read(resourceInputStream);
            ysFile.putHeader("product_key_base_url", productKeyBaseUrl);
            return ysFile;
        } catch (Exception e) {
            log.error("resourceInputStream invalid", e);
            return null;
        }
    }

    public boolean validateProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        List<Lookup> productKeyTypes = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        return productKeyTypeCodes != null
                && productKeyTypeCodes.size() > 0
                && productKeyTypeCodes.stream().allMatch(c -> productKeyTypes.stream().anyMatch(t -> t.getCode().equals(c)));
    }


    private ProductKeyBatch toProductKeyBatch(ProductKeyBatchObject object,
                                              List<Lookup> productKeyTypes,
                                              List<Lookup> productKeyBatchStatuses) {
        if (object == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(object.getId());
        batch.setBatchNo(object.getBatchNo());
        batch.setQuantity(object.getQuantity());
        batch.setStatusCode(object.getStatusCode());
        batch.setStatus(Lookup.fromCode(productKeyBatchStatuses, object.getStatusCode()));
        batch.setProductKeyTypeCodes(object.getProductKeyTypeCodes());
        batch.setProductKeyTypes(Lookup.fromCodeList(productKeyTypes, object.getProductKeyTypeCodes()));
        batch.setProductBaseId(object.getProductBaseId());
        batch.setOrgId(object.getOrgId());
        batch.setCreatedAppId(object.getCreatedAppId());
        batch.setCreatedDeviceId(object.getCreatedDeviceId());
        batch.setCreatedAccountId(object.getCreatedAccountId());
        batch.setCreatedDateTime(object.getCreatedDateTime());
        batch.setMarketingId(object.getMarketingId());
        batch.setDownloadNo(object.getDownloadNo() == null ? 0: object.getDownloadNo());
        return batch;
    }

    private ProductKeyBatch toProductKeyBatch(KeyBatch keyBatch,
                                              List<Lookup> productKeyTypes,
                                              List<Lookup> productKeyBatchStatuses) {
        if (keyBatch == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(keyBatch.getId());
        batch.setBatchNo(keyBatch.getBatchNo());
        batch.setQuantity(keyBatch.getQuantity());
        batch.setStatusCode(keyBatch.getStatusCode());
        batch.setStatus(Lookup.fromCode(productKeyBatchStatuses, keyBatch.getStatusCode()));
        batch.setProductKeyTypeCodes(keyBatch.getKeyTypeCodes());
        batch.setProductKeyTypes(Lookup.fromCodeList(productKeyTypes, keyBatch.getKeyTypeCodes()));
        batch.setProductBaseId(keyBatch.getProductBaseId());
        batch.setOrgId(keyBatch.getOrgId());
        batch.setCreatedAppId(keyBatch.getCreatedAppId());
        batch.setCreatedDeviceId(keyBatch.getCreatedDeviceId());
        batch.setCreatedAccountId(keyBatch.getCreatedAccountId());
        batch.setCreatedDateTime(keyBatch.getCreatedDateTime());
        return batch;
    }

    private String addZeroPrefix(String content, int length) {
        if (content.length() >= length)
            return content;
        else {
            String prefixZero = "";
            for (int i = 0; i < length - content.length(); i++)
                prefixZero += "0";

            return prefixZero + content;
        }
    }

}
