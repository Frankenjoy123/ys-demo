package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.OrgOrderEntity;
import com.yunsoo.data.service.entity.OrgTransactionDetailEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/16.
 */
public class OrgTransactionDetail {

    private Long id;
    private String orgId;
    private String productBaseId;
    private String batchId;
    private String accountId;
    private long mount;
    private int status;
    private String createdDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getMount() {
        return mount;
    }

    public void setMount(long mount) {
        this.mount = mount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public static OrgTransactionDetailEntity ToEntity(OrgTransactionDetail orgTransactionDetail) {
        if (orgTransactionDetail == null) return null;

        OrgTransactionDetailEntity entity = new OrgTransactionDetailEntity();
        BeanUtils.copyProperties(orgTransactionDetail, entity, new String[]{"createdDateTime"});
        if (orgTransactionDetail.getId() != null && orgTransactionDetail.getId() != 0) {
            entity.setId(orgTransactionDetail.getId());
        }
        if (orgTransactionDetail.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(DateTime.parse(orgTransactionDetail.getCreatedDateTime()));
        } else {
            entity.setCreatedDateTime(DateTime.now());
        }

        return entity;
    }

    public static OrgTransactionDetail FromEntity(OrgTransactionDetailEntity entity) {
        if (entity == null) return null;

        OrgTransactionDetail orgTransactionDetail = new OrgTransactionDetail();
        BeanUtils.copyProperties(entity, orgTransactionDetail, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            orgTransactionDetail.setCreatedDateTime(entity.getCreatedDateTime().toString());
        }
        return orgTransactionDetail;
    }

    public static List<OrgTransactionDetail> FromEntityList(Iterable<OrgTransactionDetailEntity> entityList) {
        if (entityList == null) return null;
        List<OrgTransactionDetail> orgTransactionDetailList = new ArrayList<OrgTransactionDetail>();
        for (OrgTransactionDetailEntity entity : entityList) {
            orgTransactionDetailList.add(OrgTransactionDetail.FromEntity(entity));
        }
        return orgTransactionDetailList;
    }

    public static List<OrgTransactionDetailEntity> ToEntityList(Iterable<OrgTransactionDetail> orgTransactionDetails) {
        if (orgTransactionDetails == null) return null;
        List<OrgTransactionDetailEntity> entityList = new ArrayList<OrgTransactionDetailEntity>();
        for (OrgTransactionDetail orgTransactionDetail : orgTransactionDetails) {
            entityList.add(OrgTransactionDetail.ToEntity(orgTransactionDetail));
        }
        return entityList;
    }
}
