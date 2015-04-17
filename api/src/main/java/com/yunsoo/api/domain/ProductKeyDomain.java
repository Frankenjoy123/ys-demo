package com.yunsoo.api.domain;

import com.yunsoo.api.client.processor.ProcessorClient;
import com.yunsoo.api.client.processor.message.ProductKeyBatchMassage;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final byte[] CR_LF = new byte[]{13, 10};


    //ProductKeyBatch

    public List<ProductKeyBatch> getAllProductKeyBatchesByOrgId(Integer organizationId, Long productBaseId) {
        ProductKeyBatchObject[] objects =
                dataAPIClient.get("productkeybatch?organizationId={orgid}&productBaseId={pbid}",
                        ProductKeyBatchObject[].class,
                        organizationId,
                        productBaseId);
        if (objects == null) {
            return null;
        } else {
            List<ProductKeyType> productKeyTypes = lookupDomain.getAllProductKeyTypes(null);
            return Arrays.stream(objects)
                    .map(i -> convertFromProductKeyBatchObject(i, productKeyTypes))
                    .collect(Collectors.toList());
        }
    }

    public ProductKeyBatch getProductKeyBatchById(Long id) {
        return convertFromProductKeyBatchObject(
                dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id),
                lookupDomain.getAllProductKeyTypes(true));
    }

    public ProductKeyBatch createProductKeyBatch(ProductKeyBatchObject batchObj) {
        ProductKeyBatchObject newBatchObj = dataAPIClient.post(
                "productkeybatch",
                batchObj,
                ProductKeyBatchObject.class);

        // send sqs message to processor
        ProductKeyBatchMassage sqsMessage = new ProductKeyBatchMassage();
        sqsMessage.setBatchId(newBatchObj.getId().toString());
        processorClient.post("sqs/productkeybatch", sqsMessage, String.class);

        return convertFromProductKeyBatchObject(newBatchObj, lookupDomain.getAllProductKeyTypes(true));
    }

    public byte[] getProductKeysByBatchId(long id) {
        ProductKeysObject object = dataAPIClient.get("productkeybatch/{batchId}/keys", ProductKeysObject.class, id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (object == null) {
            return null;
        }
        try {
            List<String> productKeyTypeCodes = lookupDomain.getAllProductKeyTypes(null).stream().map(LookupObject::getCode).collect(Collectors.toList());
            outputStream.write(
                    StringUtils.collectionToDelimitedString(productKeyTypeCodes, ",")
                            .getBytes(Charset.forName("UTF-8")));
            outputStream.write(CR_LF);
            for (List<String> i : object.getProductKeys()) {
                List<String> ks = i.stream().map(k -> "http://t.m.yunsu.co/" + k).collect(Collectors.toList());
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

    private ProductKeyBatch convertFromProductKeyBatchObject(ProductKeyBatchObject object, List<ProductKeyType> productKeyTypes) {
        if (object == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(object.getId());
        batch.setQuantity(object.getQuantity());
        batch.setStatusId(object.getStatusId());
        batch.setOrganizationId(object.getOrganizationId());
        batch.setCreatedClientId(object.getCreatedClientId());
        batch.setCreatedAccountId(object.getCreatedAccountId());
        batch.setCreatedDateTime(object.getCreatedDateTime());
        batch.setProductKeyTypes(LookupObject.fromCodeList(productKeyTypes, object.getProductKeyTypeCodes()));

        return batch;
    }

}
