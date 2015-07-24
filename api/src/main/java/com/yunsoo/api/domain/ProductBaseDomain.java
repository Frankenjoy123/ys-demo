package com.yunsoo.api.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.dto.ProductBaseDetails;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
public class ProductBaseDomain {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;


    public ProductBaseObject getProductBaseById(String productBaseId) {
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public ProductBaseDetails getProductBaseDetailByProductBaseIdAndVersion(String productBaseId, Integer version) throws IOException {
        try {
            String path = "photo/coms/products/" + productBaseId + "/" + version + "/nodes.json";
            FileObject fileObject = dataAPIClient.get("file?path={0}", FileObject.class, path);
            byte[] buf = fileObject.getData();
            return mapper.readValue(new ByteArrayInputStream(buf), ProductBaseDetails.class);
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "Can't find prod detail");
        }
    }

    public List<ProductBaseVersionsObject> getProductBaseVersionsByProductBaseId(String productBaseId) {
        return dataAPIClient.get("productbaseversions?product_base_Id={product_base_Id}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
        }, productBaseId);
    }

    public List<ProductBaseObject> getProductBaseByOrgId(String orgId) {
        return dataAPIClient.get("productbase?org_id={orgId}", new ParameterizedTypeReference<List<ProductBaseObject>>() {
        }, orgId);
    }

    public ProductBaseVersionsObject getProductBaseVersionsByProductBaseIdAndVersion(String productBaseId, Integer version) {
        List<ProductBaseVersionsObject> productBaseVersionsObjects = dataAPIClient.get("productbaseversions?product_base_Id={product_base_Id}&version={version}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
        }, productBaseId, version);
        if (productBaseVersionsObjects.size() == 0) {
            return null;
        }
        return productBaseVersionsObjects.get(0);
    }

    public String createProductBase(ProductBaseObject productBaseObject) {
        return dataAPIClient.post("productbase", productBaseObject, String.class);
    }

    public ProductBaseVersionsObject createProductBaseVersions(ProductBaseVersionsObject productBaseVersionsObject) {
        return dataAPIClient.post("productbaseversions/{product_base_id}", productBaseVersionsObject, ProductBaseVersionsObject.class, productBaseVersionsObject.getProductBaseId());
    }

    public void patchUpdate(ProductBaseVersionsObject productBaseVersionsObject) {
        dataAPIClient.patch("productbaseversions/{product_base_id}/{version}", productBaseVersionsObject, productBaseVersionsObject.getProductBaseId(), productBaseVersionsObject.getVersion());
    }

    public void deleteProductBase(String id) {
        dataAPIClient.delete("productbase/{id}", id);
    }

    public void deleteProductBaseVersions(String productBaseId, Integer version) {
        dataAPIClient.delete("productbaseversions/{product_base_id}/{version}", productBaseId, version);

    }

}
