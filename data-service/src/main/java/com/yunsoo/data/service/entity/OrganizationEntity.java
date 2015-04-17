package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
@Entity
@Table(name = "organization")
public class OrganizationEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private int type;

    @Column(name = "status")
    private int status;

    @Column(name = "description")
    private String description;

    @Column(name = "imageUri")
    private String imageUri;

    @Column(name = "details")
    private String details;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
