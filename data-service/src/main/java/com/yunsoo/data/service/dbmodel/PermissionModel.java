package com.yunsoo.data.service.dbmodel;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * KB on 3/8/2015
 */
@Entity
@Table(name = "permission")
@XmlRootElement
@DynamicUpdate
public class PermissionModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "order")
    private long order;

    @Column(name = "resource")
    private String resource;

    @Column(name = "action")
    private String action;

    @Column(name = "created_ts", updatable = false, nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdTs;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getGroupId() { return groupId; }
    public void setGroupId(long groupId) { this.groupId = groupId; }

    public long getOrder() { return order; }
    public void setOrder(long order) { this.order = order; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public DateTime getCreatedTs() { return createdTs; }
    public void setCreatedTs(DateTime createdTs) { this.createdTs = createdTs; }

}