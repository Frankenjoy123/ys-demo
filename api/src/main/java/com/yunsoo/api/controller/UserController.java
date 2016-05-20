package com.yunsoo.api.controller;

import com.yunsoo.api.domain.UserDomain;
import com.yunsoo.api.dto.User;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.client.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDomain userDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> queryUser(@RequestParam(value = "sex", required = false) Boolean sex,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "province", required = false) String province,
                                @RequestParam(value = "city", required = false) String city,
                                @RequestParam(value = "age_start", required = false) Integer ageStart,
                                @RequestParam(value = "age_end", required = false) Integer ageEnd,
                                @RequestParam(value = "create_datetime_start", required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                @RequestParam(value = "create_datetime_end", required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                Pageable pageable,
                                HttpServletResponse response) {

        Page<UserObject> entityPage = userDomain.getUserList(sex, phone, name, province, city, ageStart, ageEnd, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(User::new)
                .collect(Collectors.toList());
    }
}
