package com.yunsoo.service.Impl;

import com.yunsoo.dbmodel.ProductKeyBatchModel;
import com.yunsoo.dbmodel.ProductKeyModel;
import com.yunsoo.service.ProductKeyService;
import com.yunsoo.dao.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */

@Service("productKeyService")
public class ProductKeyServiceImpl implements ProductKeyService {

	@Autowired
	private ProductKeyDao productkeyDao;

    @Autowired
    private ProductKeyBatchDao productkeyBatchDao;

	@Override
	public void batchCreate() {
        ProductKeyBatchModel batchModel = new ProductKeyBatchModel();


	}
}