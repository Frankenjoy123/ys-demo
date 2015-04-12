package com.yunsoo.data.service.entity;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/12
 * Descriptions:
 */
@Entity
@Table(name = "permission_policy")
public class PermissionPolicyEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "resource_code")
    private String resourceCode;

    @Column(name = "action_code")
    private String actionCode;

    @Column(name = "description")
    private String description;
}
