package com.yunsoo.service.contract;

import com.yunsoo.entity.OperationRecordEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/8.
 */
public class OperationRecord {
    private Long id;
    private String digest;
    private String result;
    private Long accountId;
    private int operationType;
    private String createdDateTime;
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static OperationRecordEntity ToEntity(OperationRecord operationRecord) {
        if (operationRecord == null) return null;

        OperationRecordEntity entity = new OperationRecordEntity();
        BeanUtils.copyProperties(operationRecord, entity, new String[]{"createdDateTime", "id"});
        if (operationRecord.getId() != null && operationRecord.getId() != 0) {
            entity.setId(operationRecord.getId());
        }
        if (operationRecord.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(DateTime.parse(operationRecord.getCreatedDateTime()));
        } else {
            entity.setCreatedDateTime(DateTime.now());
        }
        return entity;
    }

    public static OperationRecord FromEntity(OperationRecordEntity entity) {
        if (entity == null) return null;

        OperationRecord operationRecord = new OperationRecord();
        BeanUtils.copyProperties(entity, operationRecord, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            operationRecord.setCreatedDateTime(entity.getCreatedDateTime().toString());
        }
        return operationRecord;
    }

    public static List<OperationRecord> FromEntityList(Iterable<OperationRecordEntity> entityList) {
        if (entityList == null) return null;
        List<OperationRecord> operationRecordList = new ArrayList<OperationRecord>();
        for (OperationRecordEntity entity : entityList) {
            operationRecordList.add(OperationRecord.FromEntity(entity));
        }
        return operationRecordList;
    }

    public static List<OperationRecordEntity> ToEntityList(Iterable<OperationRecord> operationRecordList) {
        if (operationRecordList == null) return null;
        List<OperationRecordEntity> entityList = new ArrayList<OperationRecordEntity>();
        for (OperationRecord operationRecord : operationRecordList) {
            entityList.add(OperationRecord.ToEntity(operationRecord));
        }
        return entityList;
    }
}
