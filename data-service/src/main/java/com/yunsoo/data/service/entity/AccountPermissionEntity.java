package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/12
 * Descriptions:
 */

@Entity
@Table(name = "account_permission")
public class AccountPermissionEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "resource_code")
    private String resourceCode;

    @Column(name = "action_code")
    private String actionCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDatetime;

    @Column(name = "created_by")
    private Long createdBy;
}
