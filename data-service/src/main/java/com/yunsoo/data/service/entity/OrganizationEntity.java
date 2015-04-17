package com.yunsoo.data.service.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
@Entity
@Table(name = "organization")
public class OrganizationEntity {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String detail;
    private int type;
    private int status;

}
