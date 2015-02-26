package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.ProductKey;
import com.yunsoo.dataapi.dto.ProductKeyBatchDto;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.ProductService;
import com.yunsoo.service.contract.ProductKeyBatch;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
//import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    private final ProductKeyBatchService productKeyBatchService;

    private final ProductService productService;

    @Autowired
    ProductKeyController(ProductKeyBatchService productKeyBatchService, ProductService productService) {
        this.productKeyBatchService = productKeyBatchService;
        this.productService = productService;
    }

    @RequestMapping(value = "/newkey", method = RequestMethod.GET)
    public ProductKey newProductKey() {
        return new ProductKey("EH6MhZATukqeKRADNOiBng", "12345");
    }

    //Response for Scanning key .
    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    public String getScanInfor(@PathVariable(value = "key") String key) {
        //to-do
        return "{  result: 真品" +
                " companyIcon: 'http://abc.com' " +
                " productIcon: 'http://abc.com/8' " +
                " Scan: 杭州西湖区X用户扫描可口可乐，  2015年1月28号 " +
                " logistic: 杭州富阳仓库，  2015年1月27号 " +
                " product: 可口可乐，水，食品添加等等。" +
                "}";
    }

    //batch request product keys
    @RequestMapping(value = "/batch/request", method = RequestMethod.POST)
    public ProductKeyBatchDto requestKeys(@RequestBody ProductKeyBatchDto batch) {
        int quantity = batch.getQuantity();
        int[] productKeyTypeIds = batch.getProductKeyTypeIds();
        int baseProductId = batch.getBaseProductId();
        int createdClientId = 1;
        int createdAccountId = 1000;
        DateTime createdDateTime = DateTime.now();

        ProductKeyBatch keyBatch = new ProductKeyBatch();
        keyBatch.setQuantity(quantity);
        keyBatch.setProductKeyTypeIds(productKeyTypeIds);
        keyBatch.setCreatedClientId(createdClientId);
        keyBatch.setCreatedAccountId(createdAccountId);
        keyBatch.setCreatedDateTime(createdDateTime);

        //create product keys
        ProductKeyBatch resultBatch = productKeyBatchService.create(keyBatch);
        if (resultBatch != null) {
            if (baseProductId > 0) {
                //create products
                List<String> keys = resultBatch.getProductKeys()
                        .stream()
                        .map(kl -> kl.get(0))
                        .collect(Collectors.toList());
                productService.batchCreate(baseProductId, keys);
            }

            ProductKeyBatchDto resultDto = new ProductKeyBatchDto();
            resultDto.setId(resultBatch.getId());
            resultDto.setQuantity(resultBatch.getQuantity());
            resultDto.setProductKeyTypeIds(resultBatch.getProductKeyTypeIds());
            resultDto.setCreatedDateTime(resultBatch.getCreatedDateTime());
            resultDto.setProductKeysAddress(resultBatch.getProductKeysAddress());
            return resultDto;
        }
        return null;
    }


}
