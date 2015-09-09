package com.yunsoo.data.service.entity;

import java.io.Serializable;

/**
 * Created by yan on 9/9/2015.
 */
public class LookupPK implements Serializable{
    private String code;
    private String typeCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
