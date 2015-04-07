package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.UserLikedProduct;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Zhe on 2015/4/3.
 * <p>
 * * ErrorCode
 * 40401    :   UserLikedProduct not found!
 */
@RestController
@RequestMapping("/userliked")
public class UserLikedProductController {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductController.class);

    @RequestMapping(value = "/userid/{userid}", method = RequestMethod.GET)
    public List<UserLikedProduct> getNewMessagesByUserId(@PathVariable(value = "userid") Long userid) {
        try {
            List<UserLikedProduct> userLikedProductList = dataAPIClient.get("userliked/userid/{userid}", List.class, userid);
            if (userLikedProductList == null || userLikedProductList.size() == 0) {
                throw new NotFoundException(40401, "UserLikedProductList not found for userid = " + userid);
            }
            return userLikedProductList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "UserLikedProductList not found for useid = " + userid);
        }
    }
}
