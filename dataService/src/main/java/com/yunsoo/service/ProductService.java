package com.yunsoo.service;

import com.yunsoo.service.contract.*;

public interface ProductService {

	public Product getByKey(String key);


	public void active();
}
