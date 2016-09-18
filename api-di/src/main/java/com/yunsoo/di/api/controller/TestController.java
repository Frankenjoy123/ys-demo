package com.yunsoo.di.api.controller;

import com.yunsoo.di.dto.TestDto;
import com.yunsoo.di.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yqy09_000 on 2016/9/10.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TestDto test(@PathVariable("id") long testId)
    {
        return testService.getTestData(testId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String test()
    {
        return "test OK";
    }
}
