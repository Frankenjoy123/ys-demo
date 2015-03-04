package com.yunsoo.dbmodel;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;
import java.util.Set;

/**
 * Created by Chen Jerry on 3/3/2015.
 */
@Entity
@Table(name = "logistics_check_point")
public class LogisticsCheckPointModel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int Id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "region_id")
    private int regionId;

    @Column(name = "org_id")
    private int orgId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRegion_id() {
        return regionId;
    }

    public void setRegion_id(int region_id) {
        this.regionId = region_id;
    }

    public int getOrg_id() {
        return orgId;
    }

    public void setOrg_id(int org_id) {
        this.orgId = org_id;
    }

}
