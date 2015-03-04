package com.yunsoo.dbmodel;

import javax.persistence.*;

/**
 * Created by Chen Jerry on 3/3/2015.
 */
@Entity
@Table(name="logistics_check_action")
public class LogisticsCheckActionModel {

    @Id
    @Column(name = "id")
    private int Id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_desc")
    private String shortDesc;

    @Column(name = "description")
    private String description;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
