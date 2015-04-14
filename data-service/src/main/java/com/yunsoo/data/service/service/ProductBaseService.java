package com.yunsoo.data.service.service;

import java.io.IOException;
import java.util.List;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.ProductBase;

public interface ProductBaseService {

    public ProductBase getById(long id);

    public S3Object getProductThumbnail(String bucketName, String key) throws IOException;

    public Long save(ProductBase productBase);

    public void update(ProductBase productBase);

    public void patchUpdate(ProductBase productBase);

    public void delete(ProductBase productBase);

    public void delete(long id);

    public void deactivate(long id);

    public List<ProductBase> getByFilter(Long orgId, Integer categoryId, Boolean active);

    public List<ProductBase> getAll();
}
