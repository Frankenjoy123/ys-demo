package com.yunsoo.api.domain;

import com.yunsoo.api.client.ProcessorClient;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchStatus;
import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeyDomain.class);

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;

    @Autowired
    private ProductKeyTransactionDomain productKeyTransactionDomain;


    @Value("${yunsoo.api.product_key_base_url}")
    private String productKeyBaseUrl;


    //ProductKey

    public ProductKeyObject getProductKeyByKey(String productKey) {
        try {
            return dataAPIClient.get("productkey/{key}", ProductKeyObject.class, productKey);
        } catch (NotFoundException ex) {
            return null;
        }
    }


    //ProductKeyBatch

    public ProductKeyBatch getProductKeyBatchById(String id) {
        return toProductKeyBatch(
                dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id),
                lookupDomain.getProductKeyTypes(),
                lookupDomain.getProductKeyBatchStatuses());
    }

    public Page<ProductKeyBatch> getProductKeyBatchesByFilterPaged(String orgId, String productBaseId, Pageable pageable) {
        String[] statusCodes = new String[]{
                LookupCodes.ProductKeyBatchStatus.CREATING,
                LookupCodes.ProductKeyBatchStatus.AVAILABLE
        };

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .append("status_code_in", statusCodes)
                .append(pageable)
                .build();
        Page<ProductKeyBatchObject> objectsPage = dataAPIClient.getPaged("productkeybatch" + query, new ParameterizedTypeReference<List<ProductKeyBatchObject>>() {
        });

        List<ProductKeyType> productKeyTypes = lookupDomain.getProductKeyTypes();
        List<ProductKeyBatchStatus> productKeyBatchStatuses = lookupDomain.getProductKeyBatchStatuses();
        return objectsPage.map(i -> toProductKeyBatch(i, productKeyTypes, productKeyBatchStatuses));
    }

    public Long sumQuantity(String orgId, String productBaseId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("product_base_id", productBaseId)
                .build();
        return dataAPIClient.get("productkeybatch/sum/quantity" + query, Long.class);
    }

    public ProductKeyBatch createProductKeyBatch(ProductKeyBatchObject batchObj) {
        String productBaseId = batchObj.getProductBaseId();

        //check product key credit
        List<ProductKeyCredit> credits = productKeyOrderDomain.getProductKeyCredits(batchObj.getOrgId(), productBaseId == null ? "*" : productBaseId);
        Long remain = 0L;
        for (ProductKeyCredit c : credits) {
            if (c.getProductBaseId() == null || c.getProductBaseId().equals(productBaseId)) {
                remain += c.getRemain();
            }
        }
        if (remain < batchObj.getQuantity()) {
            LOGGER.warn("insufficient ProductKeyCredit");
            throw new UnprocessableEntityException("产品码可用额度不足");
        }

        //create new product key batch
        batchObj.setStatusCode(LookupCodes.ProductKeyBatchStatus.NEW);
        ProductKeyBatchObject newBatchObj = dataAPIClient.post("productkeybatch", batchObj, ProductKeyBatchObject.class);
        LOGGER.info("ProductKeyBatch created [id: {}, statusCode: {}]", newBatchObj.getId(), newBatchObj.getStatusCode());

        //purchase
        String transactionId = productKeyTransactionDomain.purchase(newBatchObj);
        LOGGER.info("ProductKeyBatch purchased [transactionId: {}]", transactionId);

        //commit transaction
        productKeyTransactionDomain.commit(transactionId);
        LOGGER.info("ProductKeyTransaction committed [transactionId: {}]", transactionId);

        //update status to creating
        newBatchObj.setStatusCode(LookupCodes.ProductKeyBatchStatus.CREATING);
        dataAPIClient.patch("productkeybatch/{id}", newBatchObj, newBatchObj.getId());
        LOGGER.info("ProductKeyBatch status changed [id: {}, statusCode: {}]", newBatchObj.getId(), newBatchObj.getStatusCode());

        //send sqs message to processor
        ProductKeyBatchMassage sqsMessage = new ProductKeyBatchMassage();
        sqsMessage.setProductKeyBatchId(newBatchObj.getId());
        if (newBatchObj.getProductBaseId() != null) {
            sqsMessage.setProductStatusCode(LookupCodes.ProductStatus.ACTIVATED);  //default activated
        }
        try {
            processorClient.post("sqs/productkeybatch", sqsMessage, ProductKeyBatchMassage.class);
            LOGGER.info("ProductKeyBatchMassage posted to sqs {}", sqsMessage);
        } catch (Exception ex) {
            LOGGER.error("ProductKeyBatchMassage posting to sqs failed [exceptionMessage: {}]", ex.getMessage());
        }

        return toProductKeyBatch(newBatchObj, lookupDomain.getProductKeyTypes(), lookupDomain.getProductKeyBatchStatuses());
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

    public boolean validateProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        List<ProductKeyType> productKeyTypes = lookupDomain.getProductKeyTypes();
        return productKeyTypeCodes != null
                && productKeyTypeCodes.size() > 0
                && productKeyTypeCodes.stream().allMatch(c -> productKeyTypes.stream().anyMatch(t -> t.getCode().equals(c)));
    }


    private ProductKeyBatch toProductKeyBatch(ProductKeyBatchObject object,
                                              List<ProductKeyType> productKeyTypes,
                                              List<ProductKeyBatchStatus> productKeyBatchStatuses) {
        if (object == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(object.getId());
        batch.setQuantity(object.getQuantity());
        batch.setStatusCode(object.getStatusCode());
        batch.setStatus(LookupObject.fromCode(productKeyBatchStatuses, object.getStatusCode()));
        batch.setProductKeyTypeCodes(object.getProductKeyTypeCodes());
        batch.setProductKeyTypes(LookupObject.fromCodeList(productKeyTypes, object.getProductKeyTypeCodes()));
        batch.setProductBaseId(object.getProductBaseId());
        batch.setOrgId(object.getOrgId());
        batch.setCreatedAppId(object.getCreatedAppId());
        batch.setCreatedAccountId(object.getCreatedAccountId());
        batch.setCreatedDateTime(object.getCreatedDateTime());
        return batch;
    }

}
