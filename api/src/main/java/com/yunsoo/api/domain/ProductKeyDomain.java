package com.yunsoo.api.domain;

import com.yunsoo.api.client.ProcessorClient;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

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
    private RestClient dataAPIClient;

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private FileDomain fileDomain;

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;

    @Autowired
    private ProductKeyTransactionDomain productKeyTransactionDomain;


    @Value("${yunsoo.product_key_base_url}")
    private String productKeyBaseUrl;


    //region ProductKey

    public ProductKeyObject getProductKey(String productKey) {
        try {
            return dataAPIClient.get("productkey/{key}", ProductKeyObject.class, productKey);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public void setProductKeyDisabled(String productKey, Boolean disabled) {
        try {
            dataAPIClient.put("productkey/{key}/disabled", disabled, productKey);
        } catch (NotFoundException ex) {
            throw new NotFoundException("product key not found. " + productKey);
        }
    }

    //endregion

    //ProductKeyBatch

    public ProductKeyBatch getProductKeyBatchById(String id) {
        return toProductKeyBatch(
                dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id),
                lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType),
                lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus));
    }

    public ProductKeyBatchObject getProductKeyBatchObjectById(String id) {
        try {
            return dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }


    public Page<ProductKeyBatch> getProductKeyBatchesByFilterPaged(String orgId, String productBaseId, Boolean isPackage, Pageable pageable) {
        String[] statusCodes = new String[]{
                LookupCodes.ProductKeyBatchStatus.CREATING,
                LookupCodes.ProductKeyBatchStatus.AVAILABLE
        };
        QueryStringBuilder query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK);
        if (isPackage != null)
            query = query.append("org_id", orgId)
                    .append("is_package", isPackage)
                    .append("status_code_in", statusCodes)
                    .append(pageable);
        else
            query = query.append("org_id", orgId)
                    .append("product_base_id", productBaseId)
                    .append("status_code_in", statusCodes)
                    .append(pageable);
        Page<ProductKeyBatchObject> objectsPage = dataAPIClient.getPaged("productkeybatch" + query.build(), new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        });

        List<Lookup> productKeyTypes = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        List<Lookup> productKeyBatchStatuses = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus);
        return objectsPage.map(i -> toProductKeyBatch(i, productKeyTypes, productKeyBatchStatuses));
    }


    public Long sumQuantity(String orgId, String productBaseId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .build();
        return dataAPIClient.get("productkeybatch/sum/quantity" + query, Long.class);
    }

    public Long sumTime(String orgId, String productBaseId, DateTime startTime, DateTime endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("start_time", startTime)
                .append("end_time", endTime)
                .build();
        return dataAPIClient.get("productkeybatch/sum/time" + query, Long.class);
    }

    public ProductKeyBatch createProductKeyBatch(ProductKeyBatchObject batchObj) {
        batchObj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        batchObj.setCreatedDateTime(DateTime.now());
        String productBaseId = batchObj.getProductBaseId();
        List<String> productKeyTypes = batchObj.getProductKeyTypeCodes();
        //boolean isPackageKey = productKeyTypes.size() == 1 && LookupCodes.ProductKeyType.PACKAGE.equals(productKeyTypes.get(0));

        //check product key credit
        List<ProductKeyCredit> credits = productKeyOrderDomain.getProductKeyCredits(batchObj.getOrgId(), productBaseId == null ? "*" : productBaseId);
        Long remain = 0L;
        for (ProductKeyCredit c : credits) {
            if (c.getProductBaseId() == null || c.getProductBaseId().equals(productBaseId)) {
                remain += c.getRemain();
            }
        }
        if (remain < batchObj.getQuantity()) {
            log.warn("insufficient ProductKeyCredit");
            throw new UnprocessableEntityException("产品码可用额度不足");
        }

        //create new product key batch
        batchObj.setStatusCode(LookupCodes.ProductKeyBatchStatus.NEW);
        ProductKeyBatchObject newBatchObj = dataAPIClient.post("productkeybatch", batchObj, ProductKeyBatchObject.class);
        log.info(String.format("ProductKeyBatch created [id: %s, statusCode: %s]", newBatchObj.getId(), newBatchObj.getStatusCode()));

        //purchase
        String transactionId = productKeyTransactionDomain.purchase(newBatchObj);
        log.info(String.format("ProductKeyBatch purchased [transactionId: %s]", transactionId));

        //commit transaction
        productKeyTransactionDomain.commit(transactionId);
        log.info(String.format("ProductKeyTransaction committed [transactionId: %s]", transactionId));

        //update status to creating
        newBatchObj.setStatusCode(LookupCodes.ProductKeyBatchStatus.CREATING);
        dataAPIClient.patch("productkeybatch/{id}", newBatchObj, newBatchObj.getId());
        log.info(String.format("ProductKeyBatch status changed [id: %s, statusCode: %s]", newBatchObj.getId(), newBatchObj.getStatusCode()));

        //send sqs message to processor
        ProductKeyBatchCreateMessage sqsMessage = new ProductKeyBatchCreateMessage();
        sqsMessage.setProductKeyBatchId(newBatchObj.getId());
        sqsMessage.setProductStatusCode(LookupCodes.ProductStatus.ACTIVATED);  //default activated

        try {
            processorClient.put("sqs/message/{payloadName}", sqsMessage, ProductKeyBatchCreateMessage.PAYLOAD_NAME);
            log.info(String.format("ProductKeyBatchCreateMessage put to sqs %s", sqsMessage));
        } catch (Exception ex) {
            log.error(String.format("ProductKeyBatchCreateMessage put to sqs failed [message: %s]", ex.getMessage()), ex);
        }

        return toProductKeyBatch(newBatchObj, lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType), lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyBatchStatus));
    }

    public void patchUpdateProductKeyBatch(ProductKeyBatchObject batchObj) {
        dataAPIClient.patch("productkeybatch/{id}", batchObj, batchObj.getId());
    }

    public void putMarketingId(String id, String marketingId) {
        dataAPIClient.put("productkeybatch/{id}/marketing_id", marketingId, id);
    }


    public byte[] getProductKeysByBatchId(String id) {
        ProductKeysObject object = dataAPIClient.get("productkeybatch/{batchId}/keys", ProductKeysObject.class, id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (object == null) {
            return null;
        }
        try {
//            List<String> productKeyTypeCodes = object.getProductKeyTypeCodes();
//            outputStream.write(
//                    StringUtils.collectionToDelimitedString(productKeyTypeCodes, ",")
//                            .getBytes(Charset.forName("UTF-8")));
//            outputStream.write(CR_LF);
            for (List<String> i : object.getProductKeys()) {
                List<String> ks = i.stream().map(k -> productKeyBaseUrl + k).collect(Collectors.toList());
                outputStream.write(
                        StringUtils.collectionToDelimitedString(ks, ",")
                                .getBytes(Charset.forName("UTF-8")));
                outputStream.write(CR_LF);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("product keys format issue");
        }
        return outputStream.toByteArray();
    }

    public YSFile getProductKeyFile(String batchId, String orgId) {
        String path = String.format("organization/%s/product_key_batch/%s/keys.pks", orgId, batchId);
        ResourceInputStream resourceInputStream = fileDomain.getFile(path);
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
        batch.setCreatedAccountId(object.getCreatedAccountId());
        batch.setCreatedDateTime(object.getCreatedDateTime());
        batch.setMarketingId(object.getMarketingId());
        return batch;
    }

}
