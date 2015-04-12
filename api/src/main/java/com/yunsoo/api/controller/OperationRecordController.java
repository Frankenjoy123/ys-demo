package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.OperationRecord;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/8.
 */
@RestController
@RequestMapping("/operation/record")
public class OperationRecordController {
    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRecordController.class);

    @RequestMapping(value = "/type/{typeid}/account/{accountid}", method = RequestMethod.GET)
    public List<OperationRecord> filterOperationRecord(@PathVariable(value = "typeid") Integer typeid,
                                                       @PathVariable(value = "accountid") Long accountid) {
        if (typeid == null || typeid <= 0) throw new BadRequestException("TypeID不能小于0！");
        if (accountid == null || accountid <= 0) throw new BadRequestException("AccountID不能小于0！");
        try {
            List<OperationRecord> operationRecordList = dataAPIClient.get("/operation/record/type/{typeid}/account/{accountid}", List.class, typeid, accountid);
            if (operationRecordList == null || operationRecordList.size() == 0) {
                throw new NotFoundException(40401, "OperationRecord not found for accountid = " + accountid);
            }
            return operationRecordList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "OperationRecord not found for accountid = " + accountid);
        }
    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createRecord(@RequestBody OperationRecord operationRecord) throws Exception {
        long id = dataAPIClient.post("/operation/record/insert", operationRecord, Long.class);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }
}