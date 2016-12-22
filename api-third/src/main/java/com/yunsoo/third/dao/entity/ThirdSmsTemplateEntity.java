package com.yunsoo.third.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by yan on 6/13/2016.
 */
@Entity
@Table(name = "third_sms_template")
public class ThirdSmsTemplateEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.third.dao.util.IdGenerator")
    @Column(name = "id")
    private String id;


    @Column(name = "supplier")
    private String supplier;


    @Column(name = "supplier_id")
    private int supplierId;

    @Column(name = "name")
    private String name;

    @Column(name = "variable")
    private String variable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
