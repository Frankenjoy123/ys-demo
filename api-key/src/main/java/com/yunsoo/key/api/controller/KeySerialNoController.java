package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.key.dto.KeySerialNo;
import com.yunsoo.key.service.KeySerialNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-12-20
 * Descriptions:
 */
@RestController
@RequestMapping("/keySerialNo")
public class KeySerialNoController {

    @Autowired
    private KeySerialNoService keySerialNoService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<KeySerialNo> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                         @RequestParam(value = "org_id_in", required = false) List<String> orgIdIn) {
        if (!StringUtils.isEmpty(orgId)) {
            orgIdIn = Collections.singletonList(orgId);
        }
        if (orgIdIn != null && orgIdIn.size() > 0) {
            return keySerialNoService.getByOrgIdIn(orgIdIn);
        } else {
            throw new BadRequestException("org_id is required");
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void putOnOrgId(@RequestParam(value = "org_id") String orgId,
                           @RequestBody @Valid KeySerialNo keySerialNo) {
        if (StringUtils.isEmpty(orgId)) {
            throw new BadRequestException("org_id is null or empty");
        }
        keySerialNo.setOrgId(orgId);
        KeySerialNo result = keySerialNoService.save(keySerialNo);
        if (result == null) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void patchUpdateByOrgId(@RequestParam(value = "org_id") String orgId,
                                   @RequestBody KeySerialNo keySerialNo) {
        if (StringUtils.isEmpty(orgId)) {
            throw new BadRequestException("org_id is null or empty");
        }
        keySerialNo.setOrgId(orgId);
        keySerialNoService.patchUpdateKeySerialNo(keySerialNo);
    }

}
