package com.yunsoo.service.contract;


import com.yunsoo.dbmodel.OrganizationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KB
 * Updated by Jerry
 */
public class Organization {
    private long id;
    private String name;
    private String description;
    private String imageUrl;
    private String detail;
    private int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static Organization FromModel(OrganizationModel model) {
        if (model == null) return null;
        Organization org = new Organization();
        org.setId(model.getId());
        org.setName(model.getName());
        org.setDescription(model.getDescription());
        org.setImageUrl(model.getImageUrl());
        org.setDetail(model.getDetail());
        org.setType(model.getType());

        return org;
    }

    public static OrganizationModel ToModel(Organization org) {
        if (org == null) return null;
        OrganizationModel model = new OrganizationModel();
        if (org.getId() >= 0) {
            model.setId(org.getId());
        }
        model.setName(org.getName());
        model.setDescription(org.getDescription());
        model.setImageUrl(org.getImageUrl());
        model.setDetail(org.getDetail());
        model.setType(org.getType());

        return model;
    }

    public static List<Organization> FromModelList(List<OrganizationModel> modelList) {
        if (modelList == null) return null;
        List<Organization> orgList = new ArrayList<Organization>();
        for (OrganizationModel model : modelList) {
            orgList.add(Organization.FromModel(model));
        }

        return orgList;
    }

    public static List<OrganizationModel> ToModelList(List<Organization> orgList) {
        if (orgList == null) return null;
        List<OrganizationModel> modelList = new ArrayList<OrganizationModel>();
        for (Organization org : orgList) {
            modelList.add(Organization.ToModel(org));
        }

        return modelList;
    }
}
