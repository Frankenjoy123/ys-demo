package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserLikedProductDomain;
import com.yunsoo.api.rabbit.dto.basic.UserLikedProduct;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikedProductController.class);

    @RequestMapping(value = "/who/{userid}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#userid, 'UserLikedProduct', 'usercollection:read')")
    public List<UserLikedProduct> getUserCollectionById(@PathVariable(value = "userid") String userid,
                                                        @RequestParam(value = "index") Integer index,
                                                        @RequestParam(value = "size") Integer size) {
//        if (userid == null || userid.isEmpty()) {
//            throw new BadRequestException("UserId不应为空！");
//        }
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<UserLikedProduct> userLikedProductList = dataAPIClient.get("/user/collection/userid/{0}?index={1}&size={2}", new ParameterizedTypeReference<List<UserLikedProduct>>() {
        }, userid, index, size);

        //fill product name
        HashMap<String, ProductBaseObject> productHashMap = new HashMap<>();
        for (UserLikedProduct userLikedProduct : userLikedProductList) {
            if (!productHashMap.containsKey(userLikedProduct.getBaseProductId())) {
                ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, userLikedProduct.getBaseProductId());
                if (productBaseObject != null) {
                    productHashMap.put(userLikedProduct.getBaseProductId(), productBaseObject);
                    userLikedProduct.setProductName(productBaseObject.getName());
                    userLikedProduct.setComment(productBaseObject.getComments());
                }
            } else {
                userLikedProduct.setProductName(productHashMap.get(userLikedProduct.getBaseProductId()).getName());
                userLikedProduct.setComment(productHashMap.get(userLikedProduct.getBaseProductId()).getComments());
            }
        }
//            if (userLikedProductList == null || userLikedProductList.size() == 0) {
//                throw new NotFoundException(40401, "UserLikedProductList not found for userid = " + userid);
//            }
        return userLikedProductList;

    }

    @RequestMapping(value = "/who/{id}/product/{pid}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'UserLikedProduct', 'usercollection:read')")
    public UserLikedProduct getUserLikedProduct(@PathVariable(value = "id") String id,
                                                @PathVariable(value = "pid") String pid) {
        UserLikedProduct userLikedProduct = dataAPIClient.get("/user/collection/who/{id}/product/{pid}", UserLikedProduct.class, id, pid);
        if (userLikedProduct == null) {
            throw new NotFoundException("找不到用户收藏的产品！");
        }
        return userLikedProduct;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#userLikedProduct, 'authenticated')")
    public long createUserLikedProduct(@RequestBody UserLikedProduct userLikedProduct) throws Exception {
        if (userLikedProduct == null) {
            throw new BadRequestException("UserLikedProduct 不能为空！");
        }
        return userLikedProductDomain.ensureUserLikedProduct(userLikedProduct);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasPermission(#userLikedProduct, 'authenticated')")
    public void deleteUserLikedProduct(@RequestBody UserLikedProduct userLikedProduct) throws Exception {
        if (userLikedProduct == null) {
            throw new BadRequestException("userLikedProduct不能为空！");
        }
        UserLikedProduct productToDelete = dataAPIClient.get("/user/collection/who/{id}/product/{pid}", UserLikedProduct.class,
                userLikedProduct.getUserId(), userLikedProduct.getBaseProductId());
        if (productToDelete == null) {
            throw new NotFoundException(40401, "UserLikedProduct not found!");
        }
        if (!userDomain.validateUser(productToDelete.getUserId())) {
            throw new UnauthorizedException("不能删除其他用户的收藏信息！");
        }
        productToDelete.setActive(false); //just mark as inactive
        dataAPIClient.patch("/user/collection/", productToDelete);
    }
}