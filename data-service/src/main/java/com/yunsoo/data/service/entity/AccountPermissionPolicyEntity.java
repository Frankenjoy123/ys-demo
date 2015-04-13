package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@Entity
@Table(name = "account_permission_policy")
public class AccountPermissionPolicyEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "policy_code")
    private String policyCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDatetime;

    @Column(name = "created_by")
    private Long createdBy;
}
