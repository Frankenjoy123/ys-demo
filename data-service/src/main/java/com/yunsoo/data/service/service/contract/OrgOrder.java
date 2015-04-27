package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.OrgOrderEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/16.
 */
public class OrgOrder {

    private Long id;
    //    private Long orderId;
    private String orgId;
    private String productBaseId;
    private String batchId;
    private long total;
    private long remain;
    private String createdBy;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getRemain() {
        return remain;
    }

    public void setRemain(long remain) {
        this.remain = remain;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public static OrgOrderEntity ToEntity(OrgOrder orgOrder) {
        if (orgOrder == null) return null;

        OrgOrderEntity entity = new OrgOrderEntity();
        BeanUtils.copyProperties(orgOrder, entity, new String[]{"createdDateTime"});
        if (orgOrder.getId() != null && orgOrder.getId() != 0) {
            entity.setId(orgOrder.getId());
        }
        if (orgOrder.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(DateTime.parse(orgOrder.getCreatedDateTime()));
        } else {
            entity.setCreatedDateTime(DateTime.now());
        }

        return entity;
    }

    public static OrgOrder FromEntity(OrgOrderEntity entity) {
        if (entity == null) return null;

        OrgOrder orgOrder = new OrgOrder();
        BeanUtils.copyProperties(entity, orgOrder, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            orgOrder.setCreatedDateTime(entity.getCreatedDateTime().toString());
        }
        return orgOrder;
    }

    public static List<OrgOrder> FromEntityList(Iterable<OrgOrderEntity> entityList) {
        if (entityList == null) return null;
        List<OrgOrder> orgOrderList = new ArrayList<OrgOrder>();
        for (OrgOrderEntity entity : entityList) {
            orgOrderList.add(OrgOrder.FromEntity(entity));
        }
        return orgOrderList;
    }

    public static List<OrgOrderEntity> ToEntityList(Iterable<OrgOrder> orgOrders) {
        if (orgOrders == null) return null;
        List<OrgOrderEntity> entityList = new ArrayList<OrgOrderEntity>();
        for (OrgOrder orgOrder : orgOrders) {
            entityList.add(OrgOrder.ToEntity(orgOrder));
        }
        return entityList;
    }
}
