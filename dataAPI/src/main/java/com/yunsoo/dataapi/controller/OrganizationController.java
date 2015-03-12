package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.OrganizationService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private final OrganizationService organizationService;

    @Autowired
    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Organization> getOrganizationById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<Organization>(organizationService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Organization> getOrganizationByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<Organization>(organizationService.get(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createOrganization(@RequestBody Organization org) {
        long id = organizationService.save(org);
        HttpStatus status = id > 0 ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ResultWrapper> updateOrganization(@RequestBody Organization org) {
        Boolean result = organizationService.update(org).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteOrganization(@PathVariable(value = "id") int id) {
        Boolean result = organizationService.delete(id, 5); //delete status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}
