package com.yunsoo.service;

import com.yunsoo.service.contract.*;

import java.util.List;

public interface ProductService {

	public Product getByKey(String key);

    public void batchCreate(BaseProduct baseProduct, List<String> productKeyList);

	public void active();
}
