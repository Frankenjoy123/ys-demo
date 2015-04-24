package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserLikedProductDomain;
import com.yunsoo.api.rabbit.dto.basic.UserLikedProduct;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/3.
 * Descriptions: This controller manage end User Collections..
 * Only authorized user can consume it.
 * * ErrorCode
 * 40401    :   UserLikedProduct not found!
 */
@RestController
@RequestMapping("/user/collection")
public class UserLikedProductController {

    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private UserDomain userDomain;
    @Autowired
    private UserLikedProductDomain userLikedProductDomain;
    private final String AUTH_HEADER_NAME = "YS_RABBIT_AUTH_TOKEN";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductController.class);

    @RequestMapping(value = "/who/{userid}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#usercollection, 'usercollection:read')")
    @PreAuthorize("hasPermission(#userid, 'UserLikedProduct', 'usercollection:read')")
    public List<UserLikedProduct> getUserCollectionById(@PathVariable(value = "userid") String userid) {
        if (userid == null || userid.isEmpty()) {
            throw new BadRequestException("UserId不应为空！");
        }
        try {
            List<UserLikedProduct> userLikedProductList = dataAPIClient.get("/user/collection/userid/{userid}", List.class, userid);
            if (userLikedProductList == null || userLikedProductList.size() == 0) {
                throw new NotFoundException(40401, "UserLikedProductList not found for userid = " + userid);
            }
            return userLikedProductList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "UserLikedProductList not found for useid = " + userid);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasPermission(#usercollection, 'usercollection:create')")
    @PreAuthorize("hasPermission(#userLikedProduct, 'authenticated')")
    public long createUserLikedProduct(@RequestBody UserLikedProduct userLikedProduct) throws Exception {
        if (userLikedProduct == null) {
            throw new BadRequestException("UserLikedProduct 不能为空！");
        }
        return userLikedProductDomain.ensureUserLikedProduct(userLikedProduct);
//
//        UserLikedProduct exsitingUserLikedProduct = dataAPIClient.get("/user/collection/userid/{userid}/product/{pid}", UserLikedProduct.class, userLikedProduct.getUserId(), userLikedProduct.getBaseProductId());
//        if (exsitingUserLikedProduct != null) {
//            return new ResponseEntity<Long>(exsitingUserLikedProduct.getId(), HttpStatus.OK);
//        } else {
//            long id = dataAPIClient.post("/user/collection", userLikedProduct, long.class);
//            return new ResponseEntity<Long>(id, HttpStatus.OK);
//        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasPermission(#userLikedProduct, 'authenticated')")
    public void deleteUserLikedProduct(@RequestHeader(AUTH_HEADER_NAME) String token,
                                       @RequestBody UserLikedProduct userLikedProduct) throws Exception {
        if (userLikedProduct == null) {
            throw new BadRequestException("userLikedProduct不能为空！");
        }
        UserLikedProduct productToDelete = dataAPIClient.get("/user/collection/userid/{id}/product/{pid}", UserLikedProduct.class,
                userLikedProduct.getUserId(), userLikedProduct.getBaseProductId());
        if (productToDelete == null) {
            throw new NotFoundException(40401, "UserLikedProduct not found!");
        }
        if (!userDomain.validateToken(token, productToDelete.getUserId())) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }
        productToDelete.setActive(false); //just mark as inactive
        dataAPIClient.patch("/user/collection/", productToDelete);
    }
}