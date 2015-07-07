package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.basic.UserLikedProduct;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Zhe on 2015/4/24.
 */
@Component
public class UserLikedProductDomain {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductDomain.class);

    public long ensureUserLikedProduct(UserLikedProduct userLikedProduct) {
        if (userLikedProduct == null) {
            throw new BadRequestException("UserLikedProduct 不能为空！");
        }

        UserLikedProduct exsitingUserLikedProduct = dataAPIClient.get("/user/collection/who/{userid}/product/{pid}", UserLikedProduct.class, userLikedProduct.getUserId(), userLikedProduct.getBaseProductId());
        if (exsitingUserLikedProduct != null) {
            if (exsitingUserLikedProduct.getActive()) {
                //just return existing ID
                return exsitingUserLikedProduct.getId();
            } else {
                //just set existing record as active
                exsitingUserLikedProduct.setActive(true);
                dataAPIClient.patch("/user/collection", exsitingUserLikedProduct, long.class);
                return exsitingUserLikedProduct.getId();
            }
        } else {
            //create new record
            userLikedProduct.setActive(true);
            long id = dataAPIClient.post("/user/collection", userLikedProduct, long.class);
            return id;
        }
    }

    public UserLikedProduct getUserLikedProduct(String userid, String productid) {
        UserLikedProduct userLikedProduct = dataAPIClient.get("/user/collection/who/{id}/product/{pid}", UserLikedProduct.class, userid, productid);
        return userLikedProduct;
    }
}
