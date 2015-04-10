package com.yunsoo.dataapi.controller;

import com.yunsoo.data.service.entity.OperationRecordEntity;
import com.yunsoo.data.service.repository.OperationRecordRepository;
import com.yunsoo.data.service.service.contract.OperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/8.
 */
@RestController
@RequestMapping("/operation/record")
public class OperationRecordController {

    @Autowired
    OperationRecordRepository operationRecordRepository;

    @RequestMapping(value = "/type/{typeid}/account/{accountid}", method = RequestMethod.GET)
    public List<OperationRecord> getUserCollectionByUserId(@PathVariable(value = "typeid") Integer typeid,
                                                           @PathVariable(value = "accountid") Long accountid) {
        Iterable<OperationRecordEntity> operationRecordEntities = operationRecordRepository.findByAccountIdAndOperationType(accountid, typeid);
        return OperationRecord.FromEntityList(operationRecordEntities);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long addOperationRecord(@RequestBody OperationRecord operationRecord) {
        OperationRecordEntity newEntity = operationRecordRepository.save(operationRecord.ToEntity(operationRecord));
        return newEntity.getId();
    }

}
