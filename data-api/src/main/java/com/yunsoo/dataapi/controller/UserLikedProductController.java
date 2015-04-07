package com.yunsoo.dataapi.controller;

import com.yunsoo.repository.UserLikedProductRepository;
import com.yunsoo.service.contract.UserLikedProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/3.
 */
@RestController
@RequestMapping("/userliked")
public class UserLikedProductController {
    @Autowired
    private UserLikedProductRepository userLikedProductRepository;

    @RequestMapping(value = "/userid/{id}", method = RequestMethod.GET)
    public List<UserLikedProduct> getNewMessagesByMessageId(@PathVariable(value = "id") Long id) {
        return UserLikedProduct.FromEntityList(userLikedProductRepository.findByUserId(id));
    }
}
