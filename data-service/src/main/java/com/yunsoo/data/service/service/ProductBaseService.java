package com.yunsoo.data.service.service;

import java.io.IOException;
import java.util.List;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.ProductBase;

public interface ProductBaseService {

    public ProductBase getById(String id);

    public S3Object getProductThumbnail(String bucketName, String key) throws IOException;

    public String save(ProductBase productBase);

    public void update(ProductBase productBase);

    public void patchUpdate(ProductBase productBase);

    public void delete(ProductBase productBase);

    public void delete(String id);

    public void deactivate(String id);

    public List<ProductBase> getByFilter(String orgId, Integer categoryId, List<String> statuses);

    public List<ProductBase> getAll();
}
