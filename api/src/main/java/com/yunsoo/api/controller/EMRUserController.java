package com.yunsoo.api.controller;

import com.yunsoo.api.domain.EMRUserDomain;
import com.yunsoo.api.domain.UserBlockDomain;
import com.yunsoo.api.dto.EMRUser;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.EMRUserObject;
import com.yunsoo.common.data.object.UserBlockObject;
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

@RestController
@RequestMapping("/emr/user")
public class EMRUserController {

    @Autowired
    private EMRUserDomain emrUserDomain;

    @Autowired
    private UserBlockDomain userBlockDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<EMRUser> queryUser(@RequestParam(value = "org_id", required = false) String orgId,
                                   @RequestParam(value = "sex", required = false) Boolean sex,
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

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRUserList(orgId, sex, phone, name, province, city, ageStart, ageEnd, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        List<EMRUser> emrUsers = entityPage.map(emr -> {
            EMRUser user = new EMRUser(emr);

            List<UserBlockObject> userBlockObjects = userBlockDomain.getUserBlockList(user.getUserId(), user.getYsId(), user.getOrgId());
            if (userBlockObjects == null || userBlockObjects.size() == 0) {
                user.setUserBlockId("");
            } else {
                user.setUserBlockId(userBlockObjects.get(0).getId());
            }

            return user;
        }).getContent();

        return emrUsers;
    }
}
