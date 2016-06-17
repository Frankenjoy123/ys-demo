package com.yunsoo.api.controller;

import com.yunsoo.api.domain.UserBlockDomain;
import com.yunsoo.api.dto.UserBlock;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.UserBlockObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/block")
public class UserBlockController {

    @Autowired
    private UserBlockDomain userBlockDomain;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#userBlock.orgId, 'org', 'user_block:write')")
    public UserBlock create(@RequestBody UserBlock userBlock) {

        UserBlockObject userBlockObject = new UserBlockObject();
        String currentAccountId = AuthUtils.getCurrentAccount().getId();

        userBlockObject.setId(null);
        userBlockObject.setUserId(userBlock.getUserId());
        userBlockObject.setYsId(userBlock.getYsId());
        userBlockObject.setCreatedAccountId(currentAccountId);
        userBlockObject.setCreatedDateTime(DateTime.now());

        if (StringUtils.hasText(userBlock.getOrgId()))
            userBlockObject.setOrgId(userBlock.getOrgId());
        else
            userBlockObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        UserBlockObject newUserBlockObject = userBlockDomain.create(userBlockObject);

        return new UserBlock(newUserBlockObject);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void delete(@RequestParam(value = "user_block_id", required = false) String userBlockId) {

        userBlockDomain.delete(userBlockId);
    }
}
