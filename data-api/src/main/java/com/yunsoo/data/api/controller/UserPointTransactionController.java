package com.yunsoo.data.api.controller;

import com.yunsoo.data.service.repository.UserPointTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
@RestController
@RequestMapping("/userpointtransaction")
public class UserPointTransactionController {

    @Autowired
    private UserPointTransactionRepository userPointTransactionRepository;


}
