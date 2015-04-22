package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.AccountEntity;
import com.yunsoo.data.service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : KB
 * Created on  : 3/13/2015
 * Descriptions:
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public AccountObject getById(@PathVariable String id) {
        AccountEntity entity = accountRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("Account not found by [id: " + id + "]");
        }
        return entityToObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountObject> getByFilter(@RequestParam("org_id") String orgId,
                                           @RequestParam(value = "identifier", required = false) String identifier) {
        List<AccountEntity> entities = identifier == null
                ? accountRepository.findByOrgId(orgId)
                : accountRepository.findByOrgIdAndIdentifier(orgId, identifier);

        return entities.stream().map(this::entityToObject).collect(Collectors.toList());
    }


    private AccountObject entityToObject(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountObject object = new AccountObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setIdentifier(entity.getIdentifier());
        object.setStatusCode(entity.getStatusCode());
        object.setFirstName(entity.getFirstName());
        object.setLastName(entity.getLastName());
        object.setEmail(entity.getEmail());
        object.setPhone(entity.getPhone());
        object.setPassword(entity.getPassword());
        object.setHashSalt(entity.getHashSalt());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDatetime(entity.getModifiedDatetime());
        return object;
    }
}
