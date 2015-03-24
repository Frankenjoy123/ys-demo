package com.yunsoo.service.contract.lookup;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public abstract class AbstractLookup {

    private Integer id;
    private String code;
    private String name;
    private String description;
    private Boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public static <T extends AbstractLookup, M extends AbstractLookupModel> M toModel(T lookup){
//        lookup.getModelType();
//    }
//
//    public static <T extends AbstractLookup, M extends AbstractLookupModel> T fromModel(M model){
//
//    }
}
