package com.yunsoo.api.dto.basic;

import com.yunsoo.common.data.object.ProductBaseObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Zhe on 2015/3/19.
 */
public class ProductBase extends ProductBaseObject {

    @Autowired
    private String productBasePicURL;

    private String thumbnailURL;

    public String getThumbnailURL() {
        return productBasePicURL + "id" + this.getId() + ".jpg";
    }
}
