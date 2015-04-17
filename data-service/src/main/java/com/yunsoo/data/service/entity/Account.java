package com.yunsoo.data.service.entity;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
@Entity
@Table(name = "account")
public class Account {
    private Long id;
    private Integer status;
    private String SSID;
    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String salt;
    private DateTime createdTs;
    private DateTime updatedTs;
    private Long updatedBy;
}
