package com.yunsoo.common.data;

/**
 * Created by yan on 9/14/2015.
 */
public enum CacheType {

    ORGANIZATION("organization"),
    USER("user"),
    ACCOUNT("account"),
    LOOKUP("lookup"),
    PRODUCTBASE("productbase"),
    PRODUCT("product"),
    PRODUCT_BATCH("product_batch"),
    PRODUCTCATEGORY("product_category"),
    PERMISSION("permission");


    private String typeName;

    CacheType(String type){
        this.typeName = type;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
