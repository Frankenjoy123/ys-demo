package com.yunsoo.marketing.service;

import com.yunsoo.marketing.dao.repository.UserRightContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@Service
public class UserRightContactService {
    @Autowired
    private UserRightContactRepository userRightContactRepository;

}
