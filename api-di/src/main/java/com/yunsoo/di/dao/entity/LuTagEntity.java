package com.yunsoo.di.dao.entity;


import com.yunsoo.di.dto.LuTagObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/8
 * Descriptions:
 */
@Entity
@Table(name = "lu_tag")
public class LuTagEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String tagName;

    @Column(name = "category")
    private int category;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public static LuTagObject toDataObject(LuTagEntity luTagEntity) {
        LuTagObject object = new LuTagObject();
        object.setTagName(luTagEntity.getTagName());
        object.setId(luTagEntity.getId());
        object.setCategory(luTagEntity.getCategory());
        return object;
    }
}
