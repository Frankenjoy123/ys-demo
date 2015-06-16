package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.AccountEntity;
import com.yunsoo.data.service.repository.AccountRepository;
import com.yunsoo.data.service.repository.OrganizationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Autowired
    private OrganizationRepository organizationRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public AccountObject getById(@PathVariable String id) {
        AccountEntity entity = accountRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("Account not found by [id: " + id + "]");
        }
        return toAccountObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountObject> getByFilter(@RequestParam("org_id") String orgId,
                                           @RequestParam(value = "identifier", required = false) String identifier) {
        List<AccountEntity> entities = identifier == null
                ? accountRepository.findByOrgId(orgId)
                : accountRepository.findByOrgIdAndIdentifier(orgId, identifier);

        return entities.stream().map(this::toAccountObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "pageable", method = RequestMethod.GET)
    public List<AccountObject> getByFilter(@RequestParam("org_id") String orgId,
                                           @PageableDefault(size = 1000)
                                           Pageable pageable,
                                           HttpServletResponse response) {

        Page<AccountEntity> entities = accountRepository.findByOrgId(orgId,pageable);

        response.setHeader("Content-Range", "pages " + entities.getNumber() + "/" + entities.getTotalPages());

        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toAccountObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "count/id", method = RequestMethod.GET)
    public Long count(@RequestParam(value = "org_id", required = false) String orgId,
                      @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        if (orgId == null) {
            if (statusCodeIn == null) {
                return accountRepository.count();
            } else {
                return accountRepository.countByStatusCodeIn(statusCodeIn);
            }
        } else {
            if (statusCodeIn == null) {
                return accountRepository.countByOrgId(orgId);
            } else {
                return accountRepository.countByOrgIdAndStatusCodeIn(orgId, statusCodeIn);
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountObject create(@RequestBody AccountObject accountObject) {
        AccountEntity entity = toAccountEntity(accountObject);
        if (organizationRepository.findOne(entity.getOrgId()) == null) {
            throw new BadRequestException("Can not create account without organization");
        }
        if (accountRepository.findByOrgIdAndIdentifier(entity.getOrgId(), entity.getIdentifier()).size() > 0) {
            throw new BadRequestException("identifier already exists in the same organization");
        }
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setId(null);
        entity.setModifiedAccountId(null);
        entity.setModifiedDatetime(null);
        return toAccountObject(accountRepository.save(entity));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable String id, @RequestBody AccountObject accountObject) {
        AccountEntity entity = accountRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("Account not found by [id: " + id + "]");
        }
        //identifier
        String identifier = accountObject.getIdentifier();
        if (identifier != null && !identifier.equals(entity.getIdentifier())) {
            if (accountRepository.findByOrgIdAndIdentifier(entity.getOrgId(), accountObject.getIdentifier()).size() > 0) {
                throw new BadRequestException("identifier already exists in the same organization");
            }
            entity.setIdentifier(accountObject.getIdentifier());
        }
        if (accountObject.getStatusCode() != null) {
            entity.setStatusCode(accountObject.getStatusCode());
        }
        if (accountObject.getFirstName() != null) {
            entity.setFirstName(accountObject.getFirstName());
        }
        if (accountObject.getLastName() != null) {
            entity.setLastName(accountObject.getLastName());
        }
        if (accountObject.getEmail() != null) {
            entity.setEmail(accountObject.getEmail());
        }
        if (accountObject.getPhone() != null) {
            entity.setPhone(accountObject.getPhone());
        }
        if (accountObject.getPassword() != null && accountObject.getHashSalt() != null) {
            entity.setPassword(accountObject.getPassword());
            entity.setHashSalt(accountObject.getHashSalt());
        }
        entity.setModifiedAccountId(accountObject.getModifiedAccountId());
        entity.setModifiedDatetime(accountObject.getModifiedDatetime() == null
                ? DateTime.now() : accountObject.getModifiedDatetime());

        accountRepository.save(entity);
    }

    private AccountObject toAccountObject(AccountEntity entity) {
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

    private AccountEntity toAccountEntity(AccountObject object) {
        if (object == null) {
            return null;
        }
        AccountEntity entity = new AccountEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setIdentifier(object.getIdentifier());
        entity.setStatusCode(object.getStatusCode());
        entity.setFirstName(object.getFirstName());
        entity.setLastName(object.getLastName());
        entity.setEmail(object.getEmail());
        entity.setPhone(object.getPhone());
        entity.setPassword(object.getPassword());
        entity.setHashSalt(object.getHashSalt());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDatetime(object.getModifiedDatetime());
        return entity;
    }

}
