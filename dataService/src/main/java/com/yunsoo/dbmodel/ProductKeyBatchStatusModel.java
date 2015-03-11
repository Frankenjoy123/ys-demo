package com.yunsoo.dbmodel;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by:   Lijian
 * Created on:   2015/3/10
 * Descriptions:
 */
@Entity
@Table(name = "product_key_batch_status")
@XmlRootElement
@DynamicUpdate
@SelectBeforeUpdate
public class ProductKeyBatchStatusModel {

    @javax.persistence.Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "description")
    private String description;
    @Column(name = "active")
    private Boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
