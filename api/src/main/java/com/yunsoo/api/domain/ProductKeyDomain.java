package com.yunsoo.api.domain;

import com.yunsoo.api.client.ProcessorClient;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
@Component("productKeyDomain")
public class ProductKeyDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private LookupDomain lookupDomain;

    private static final byte[] CR_LF = new byte[]{13, 10};

    @Value("${yunsoo.api.product_key_base_url}")
    private String productKeyBaseUrl;

    //ProductKeyBatch

    public ProductKeyBatch getProductKeyBatchById(String id) {
        return fromProductKeyBatchObject(
                dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id),
                lookupDomain.getAllProductKeyTypes(true));
    }

    public List<ProductKeyBatch> getAllProductKeyBatchesByOrgId(String orgId, String productBaseId) {
        ProductKeyBatchObject[] objects =
                dataAPIClient.get("productkeybatch?orgId={orgid}&productBaseId={pbid}",
                        ProductKeyBatchObject[].class,
                        orgId,
                        productBaseId);

        List<ProductKeyType> productKeyTypes = lookupDomain.getAllProductKeyTypes();
        return Arrays.stream(objects)
                .map(i -> fromProductKeyBatchObject(i, productKeyTypes))
                .collect(Collectors.toList());
    }

    public ProductKeyBatch createProductKeyBatch(ProductKeyBatchObject batchObj) {
        batchObj.setStatusCode(LookupCodes.ProductKeyBatchStatus.CREATING);
        ProductKeyBatchObject newBatchObj = dataAPIClient.post(
                "productkeybatch",
                batchObj,
                ProductKeyBatchObject.class);

        // send sqs message to processor
        ProductKeyBatchMassage sqsMessage = new ProductKeyBatchMassage();
        sqsMessage.setProductKeyBatchId(newBatchObj.getId());
        if (newBatchObj.getProductBaseId() != null) {
            sqsMessage.setProductStatusCode(LookupCodes.ProductStatus.ACTIVATED);  //default activated
        }
        processorClient.post("sqs/productkeybatch", sqsMessage);

        return fromProductKeyBatchObject(newBatchObj, lookupDomain.getAllProductKeyTypes(true));
    }

    public byte[] getProductKeysByBatchId(String id) {
        ProductKeysObject object = dataAPIClient.get("productkeybatch/{batchId}/keys", ProductKeysObject.class, id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (object == null) {
            return null;
        }
        try {
            List<String> productKeyTypeCodes = LookupObject.toCodeList(lookupDomain.getAllProductKeyTypes());
            outputStream.write(
                    StringUtils.collectionToDelimitedString(productKeyTypeCodes, ",")
                            .getBytes(Charset.forName("UTF-8")));
            outputStream.write(CR_LF);
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
        List<ProductKeyType> productKeyTypes = lookupDomain.getAllProductKeyTypes();
        return productKeyTypeCodes != null
                && productKeyTypeCodes.size() > 0
                && productKeyTypeCodes.stream().allMatch(c -> productKeyTypes.stream().anyMatch(t -> t.getCode().equals(c)));
    }

    private ProductKeyBatch fromProductKeyBatchObject(ProductKeyBatchObject object, List<ProductKeyType> productKeyTypes) {
        if (object == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(object.getId());
        batch.setQuantity(object.getQuantity());
        batch.setStatusCode(object.getStatusCode());
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
