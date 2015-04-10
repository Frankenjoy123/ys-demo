package com.yunsoo.dataapi.controller;


import com.yunsoo.common.data.object.PackageBoundObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotAcceptableException;
import com.yunsoo.dataapi.dto.PackageDto;
import com.yunsoo.data.service.service.ProductPackageService;
import com.yunsoo.data.service.service.contract.PackageBoundContract;
import com.yunsoo.data.service.service.contract.PackageContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


//import org.apache.http.HttpStatus;

/**
 * Created by Qiyong 2015/2/7.
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private final ProductPackageService packageService;

    @Autowired
    PackageController(ProductPackageService packageService) {
        this.packageService = packageService;
    }

    /**
     * bind the products or package relations.
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public Boolean bind(@RequestBody PackageBoundObject data) {

        PackageBoundContract contract = toServiceContract(data);
        boolean succeeded = packageService.bind(contract);
        return succeeded;
    }

    @RequestMapping(value = "/batch/bind", method = RequestMethod.POST)
    public Boolean batchBind(@RequestBody PackageBoundObject[] dataList) {
        boolean result = false;
        if (dataList != null && dataList.length > 0) {
            List<PackageBoundContract> contracts = new ArrayList<PackageBoundContract>();
            for (PackageBoundObject dto : dataList) {
                contracts.add(toServiceContract(dto));
            }
            try {
                result = packageService.batchBind(contracts.toArray(new PackageBoundContract[0]));
            } catch (IllegalArgumentException e) {
                throw new NotAcceptableException(e.getMessage());
            } catch (Exception ex) {
                throw new InternalServerErrorException(ex.getMessage());
            }

        }
        return result;
    }

    private PackageBoundContract toServiceContract(PackageBoundObject dto) {
        PackageBoundContract contract = new PackageBoundContract();
        contract.setKeys(dto.getKeys());
        contract.setOperator(dto.getOperator());
        contract.setPackageKey(dto.getPackageKey());
        contract.setCreated_date(dto.getCreated_date());
        return contract;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public PackageDto query(@PathVariable(value = "key") String key) {
        PackageContract p = packageService.query(key);
        PackageDto dto = new PackageDto(p);
        return dto;
    }

    @RequestMapping(value = "/revoke/{key}", method = RequestMethod.DELETE)
    public Boolean revokeAll(@PathVariable(value = "key") String key) {
        boolean result = packageService.revoke(key);
        return result;
    }

    @RequestMapping(value = "/list/{key}", method = RequestMethod.GET)
    public ResponseEntity<List<String>> flatQuery(@PathVariable(value = "key") String key) {
        List<String> allKeys = packageService.loadAllKeys(key);
        return new ResponseEntity<List<String>>(allKeys, HttpStatus.OK);
    }


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<String> batchFlatQuery(@RequestBody List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            throw new BadRequestException("数据为空");
        }
        List<String> allKeys = new ArrayList<String>();
        for (String key : keys) {
            List<String> itemKeys = packageService.loadAllKeys(key);
            allKeys.addAll(allKeys.size(), itemKeys);
        }
        return allKeys;
    }

}
