package com.yunsoo.key.dao.entity;

import com.yunsoo.key.dao.util.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2016-12-20
 * Descriptions:
 */
@Entity
@Table(name = "product_key_serial_no")
public class KeySerialNoEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = IdGenerator.CLASS)
    @Column(name = "id")
    private String id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "serial_length")
    private int serialLength;

    @Column(name = "offset")
    private int offset;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "suffix")
    private String suffix;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getSerialLength() {
        return serialLength;
    }

    public void setSerialLength(int serialLength) {
        this.serialLength = serialLength;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
