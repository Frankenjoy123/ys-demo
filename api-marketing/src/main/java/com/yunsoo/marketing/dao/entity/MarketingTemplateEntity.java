package com.yunsoo.marketing.dao.entity;

import com.yunsoo.marketing.dao.util.IdGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by:   Haitao
 * Created on:   2016-10-13
 * Descriptions:
 */
@Entity
@Table(name = "marketing_template")
public class MarketingTemplateEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = IdGenerator.CLASS)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
