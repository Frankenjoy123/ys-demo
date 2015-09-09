package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.LookupObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 9/8/2015.
 */
public class Lookup {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("type_code")
    private String typeCode;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Lookup(LookupObject object){
        this.setDescription(object.getDescription());
        this.setTypeCode(object.getTypeCode());
        this.setName(object.getName());
        this.setCode(object.getCode());
        this.setActive(object.getActive());
    }

    public Lookup(){}

    public static LookupObject toLookupObj(Lookup lookup){
        LookupObject obj = new LookupObject();
        obj.setActive(lookup.getActive());
        obj.setTypeCode(lookup.getTypeCode());
        obj.setDescription(lookup.getDescription());
        obj.setName(lookup.getName());
        obj.setCode(lookup.getCode());
        return obj;
    }

    public static Lookup fromCode(List<Lookup> lookup, String code) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        if (code == null) {
            return null;
        }
        for (Lookup item : lookup) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    public static List<Lookup> fromCodeList(List<Lookup> lookup, List<String> codeList) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        if (codeList == null) {
            throw new IllegalArgumentException("list is null");
        }
        return codeList.stream().map(code -> {
            if (code != null) {
                for (Lookup i : lookup) {
                    if (code.equals(i.getCode())) {
                        return i;
                    }
                }
            }
            throw new IllegalArgumentException("invalid code: " + code);
        }).collect(Collectors.toList());
    }

}
