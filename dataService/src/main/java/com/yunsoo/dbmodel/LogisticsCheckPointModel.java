//package com.yunsoo.dbmodel;
//
//import org.hibernate.annotations.Type;
//import org.joda.time.DateTime;
//import OrganizationModel;
//import javax.persistence.*;
//import java.util.Set;
//
///**
// * Qiyong Yu
// */
//@Entity
//@Table(name = "logistics_check_point")
//public class LogisticsCheckPointModel {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "id")
//    private int id;
//
//    @Column(name = "name")
//    private String name;
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "region_id")
//    private int region_id;
//
//    @Column(name = "org_id")
//    private int org_id;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public int getRegion_id() {
//        return region_id;
//    }
//
//    public void setRegion_id(int region_id) {
//        this.region_id = region_id;
//    }
//
//    public int getOrg_id() {
//        return org_id;
//    }
//
//    public void setOrg_id(int org_id) {
//        this.org_id = org_id;
//    }
//
//}
