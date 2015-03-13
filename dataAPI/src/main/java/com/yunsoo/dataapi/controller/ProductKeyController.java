package com.yunsoo.dataapi.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.dataapi.dto.ProductDto;
import com.yunsoo.dataapi.dto.ProductKeyBatchDto;
import com.yunsoo.dataapi.dto.ProductKeyBatchRequestDto;
import com.yunsoo.dataapi.dto.ProductKeyDto;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.ProductKeyService;
import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKey;
import com.yunsoo.service.contract.ProductKeyBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    private final ProductKeyService productKeyService;

    private final ProductKeyBatchService productKeyBatchService;

    @Autowired
    ProductKeyController(ProductKeyService productKeyService,
                         ProductKeyBatchService productKeyBatchService) {
        this.productKeyService = productKeyService;
        this.productKeyBatchService = productKeyBatchService;
    }

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductKeyDto getKey(@PathVariable(value = "key") String key) {
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

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        productKeyService.disable(key);
    }


    //batch

    @RequestMapping(value = "batch/{id}", method = RequestMethod.GET)
    public ProductKeyBatchDto getBatchById(@PathVariable(value = "id") String idStr) {
        int idInt;
        try {
            idInt = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("invalid id");
        }
        ProductKeyBatch batch = productKeyBatchService.getById(idInt);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        ProductKeyBatchDto batchDto = new ProductKeyBatchDto();
        batchDto.setId(batch.getId());
        batchDto.setQuantity(batch.getQuantity());
        batchDto.setStatusId(batch.getStatusId());
        batchDto.setOrganizationId(batch.getOrganizationId());
        batchDto.setCreatedClientId(batch.getCreatedClientId());
        batchDto.setCreatedAccountId(batch.getCreatedAccountId());
        batchDto.setCreatedDateTime(batch.getCreatedDateTime());
        batchDto.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        batchDto.setProductKeysAddress(batch.getProductKeysAddress());
        return batchDto;
    }


    @RequestMapping(value = "batch/create", method = RequestMethod.POST)
    public ProductKeyBatchDto batchCreateProductKeys(@RequestBody ProductKeyBatchRequestDto request) {
        ProductKeyBatchDto batchDto = request.getProductKeyBatch();
        ProductDto productDto = request.getProductTemplate();

        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setQuantity(batchDto.getQuantity());
        batch.setStatusId(batchDto.getStatusId());
        batch.setOrganizationId(batchDto.getOrganizationId());
        batch.setCreatedClientId(batchDto.getCreatedClientId());
        batch.setCreatedAccountId(batchDto.getCreatedAccountId());
        batch.setCreatedDateTime(batchDto.getCreatedDateTime());
        batch.setProductKeyTypeIds(batchDto.getProductKeyTypeIds());
        batch.setProductKeysAddress(batchDto.getProductKeysAddress());
        Product product = null;
        if (productDto != null) {
            product = new Product();
            product.setProductBaseId(productDto.getProductBaseId());
            product.setProductStatusId(productDto.getProductStatusId());
            product.setManufacturingDateTime((productDto.getManufacturingDateTime()));
            product.setCreatedDateTime(productDto.getCreatedDateTime());
        }
        ProductKeyBatch newBatch = productKeyBatchService.create(batch, product);
        ProductKeyBatchDto newBatchDto = new ProductKeyBatchDto();
        newBatchDto.setId(newBatch.getId());
        newBatchDto.setQuantity(newBatch.getQuantity());
        newBatchDto.setStatusId(newBatch.getStatusId());
        newBatchDto.setOrganizationId(newBatch.getOrganizationId());
        newBatchDto.setCreatedClientId(newBatch.getCreatedClientId());
        newBatchDto.setCreatedAccountId(newBatch.getCreatedAccountId());
        newBatchDto.setCreatedDateTime(newBatch.getCreatedDateTime());
        newBatchDto.setProductKeyTypeIds(newBatch.getProductKeyTypeIds());
        newBatchDto.setProductKeysAddress(newBatch.getProductKeysAddress());

        return newBatchDto;
    }


}
