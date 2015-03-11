package com.yunsoo.dataapi.controller;


import com.yunsoo.dataapi.dto.PackageDto;
import com.yunsoo.service.ProductPackageService;
import com.yunsoo.service.contract.PackageBoundContract;
import com.yunsoo.service.contract.PackageContract;
import com.yunsoo.dataapi.dto.PackageBoundDto;

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
    public Boolean bind(@RequestBody PackageBoundDto data) {

        PackageBoundContract contract = data.toServiceContract();
        boolean succeeded = packageService.bind(contract);
        return succeeded;
    }

    @RequestMapping(value = "/batch/bind", method = RequestMethod.POST)
    public Boolean batchBind(@RequestBody PackageBoundDto[] dataList) {
        boolean result = false;
        if (dataList != null && dataList.length > 0) {
            List<PackageBoundContract> contracts = new ArrayList<>();
            for (PackageBoundDto dto : dataList) {
                contracts.add(dto.toServiceContract());
            }
            result = packageService.batchBind(contracts.toArray(new PackageBoundContract[0]));
        }
        return result;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public PackageDto query(@PathVariable(value = "key") String key) {
        PackageContract p = packageService.query(key);
        PackageDto dto = new PackageDto(p);
        return dto;
    }

    @RequestMapping(value = "/revoke/{key}", method = RequestMethod.GET)
    public Boolean revokeAll(@PathVariable(value = "key") String key) {
        boolean result = packageService.revoke(key);
        return result;
    }

    @RequestMapping(value = "/list/{key}", method = RequestMethod.GET)
    public ResponseEntity<List<String>> flatQuery(@PathVariable(value ="key") String key)
    {
        List<String> allKeys = packageService.loadAllKeys(key);
        return new ResponseEntity<List<String>>(allKeys, HttpStatus.OK);
    }

}
