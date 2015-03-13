package com.yunsoo.dataapi.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.dataapi.dto.ProductKeyBatchDto;
import com.yunsoo.dataapi.dto.ProductKeyDto;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.ProductKeyService;
import com.yunsoo.service.ProductService;
import com.yunsoo.service.contract.ProductKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    private final ProductKeyService productKeyService;

    private final ProductKeyBatchService productKeyBatchService;

    private final ProductService productService;

    @Autowired
    ProductKeyController(ProductKeyService productKeyService,
                         ProductKeyBatchService productKeyBatchService,
                         ProductService productService) {
        this.productKeyService = productKeyService;
        this.productKeyBatchService = productKeyBatchService;
        this.productService = productService;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ProductKeyDto get(@PathVariable(value = "key") String key) {
        ProductKeyDto productKeyDto = new ProductKeyDto();
        ProductKey productKey = productKeyService.get(key);
        if (productKey == null) {
            throw new NotFoundException("ProductKey");
        }
        productKeyDto.setProductKey(productKey.getProductKey());
        productKeyDto.setProductKeyTypeId(productKey.getProductKeyTypeId());
        productKeyDto.setProductKeyDisabled(productKey.isProductKeyDisabled());
        productKeyDto.setPrimary(productKey.isPrimary());
        productKeyDto.setProductKeyBatchId(productKey.getProductKeyBatchId());
        productKeyDto.setPrimaryProductKey(productKey.getPrimaryProductKey());
        productKeyDto.setProductKeySet(productKey.getProductKeySet());
        productKeyDto.setCreatedDateTime(productKey.getCreatedDateTime());
        return productKeyDto;
    }

    //batch request product keys
    @RequestMapping(value = "/batch/request", method = RequestMethod.POST)
    public ProductKeyBatchDto requestKeys(@RequestBody ProductKeyBatchDto batch) {
//        int quantity = batch.getQuantity();
//        int[] productKeyTypeIds = batch.getProductKeyTypeIds();
//        int baseProductId = batch.getBaseProductId();
//        int createdClientId = 1;
//        int createdAccountId = 1000;
//        DateTime createdDateTime = DateTime.now();
//
//        ProductKeyBatch keyBatch = new ProductKeyBatch();
//        keyBatch.setQuantity(quantity);
//        keyBatch.setProductKeyTypeIds(productKeyTypeIds);
//        keyBatch.setCreatedClientId(createdClientId);
//        keyBatch.setCreatedAccountId(createdAccountId);
//        keyBatch.setCreatedDateTime(createdDateTime);
//
//        //create product keys
//        ProductKeyBatch resultBatch = productKeyBatchService.create(keyBatch);
//        if (resultBatch != null) {
//            if (baseProductId > 0) {
//                //create products
//                List<String> keys = resultBatch.getProductKeys()
//                        .stream()
//                        .map(kl -> kl.get(0))
//                        .collect(Collectors.toList());
//                //productService.batchCreate(baseProductId, keys);
//            }
//
//            ProductKeyBatchDto resultDto = new ProductKeyBatchDto();
//            //resultDto.setId(resultBatch.getId());
//            resultDto.setQuantity(resultBatch.getQuantity());
//            resultDto.setProductKeyTypeIds(resultBatch.getProductKeyTypeIds());
//            resultDto.setCreatedDateTime(resultBatch.getCreatedDateTime());
//            resultDto.setProductKeysAddress(resultBatch.getProductKeysAddress());
//            return resultDto;
//        }
        return null;
    }


}
