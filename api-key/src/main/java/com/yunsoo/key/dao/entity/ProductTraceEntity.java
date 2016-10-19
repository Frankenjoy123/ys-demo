package com.yunsoo.key.dao.entity;

import com.yunsoo.key.dao.util.IdGenerator;
import com.yunsoo.key.dto.ProductTrace;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by yan on 10/13/2016.
 */
@Entity
@Table(name = "product_trace")
public class ProductTraceEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = IdGenerator.CLASS)
    @Column(name = "id")
    private String id;

    @Column(name = "product_key")
    private String productKey;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "action")
    private String action;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "created_source_id")
    private String createdSourceId;

    @Column(name = "created_source_type")
    private String createdSourceType;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }



    public ProductTraceEntity(){

    }

    public String getCreatedSourceId() {
        return createdSourceId;
    }

    public void setCreatedSourceId(String createdSourceId) {
        this.createdSourceId = createdSourceId;
    }

    public String getCreatedSourceType() {
        return createdSourceType;
    }

    public void setCreatedSourceType(String createdSourceType) {
        this.createdSourceType = createdSourceType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductTraceEntity(ProductTrace trace){
        BeanUtils.copyProperties(trace, this);
    }

}
