package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.AccountEntity;
import com.yunsoo.data.service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public AccountObject getByOrgIdAndIdentifier(@RequestParam("orgId") String orgId, @RequestParam("identifier") String identifier) {
        List<AccountEntity> entities = accountRepository.findByOrgIdAndIdentifier(orgId, identifier);
        if (entities.size() == 0) {
            throw new NotFoundException("Account not found by [orgId: " + orgId + ", identifier: " + identifier + "]");
        } else if (entities.size() > 1) {
            throw new InternalServerErrorException("duplicated Account found by [orgId: " + orgId + ", identifier: " + identifier + "]");
        }
        return entityToObject(entities.get(0));
    }
//
//    @RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
//    public ResponseEntity<AccountDto> getAccountByToken(@PathVariable(value = "token") String token) {
//        return new ResponseEntity<AccountDto>(AccountDto.FromAccount(accountService.getByToken(token)), HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/identifier/{username}",method = RequestMethod.GET)
//    public ResponseEntity<AccountDto> getAccountByUsername(@PathVariable(value = "username") String username) {
//        return new ResponseEntity<AccountDto>(AccountDto.FromAccount(accountService.getByIdentifier(username)), HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/verify", method = RequestMethod.POST)
//    public ResponseEntity<Boolean> verify(@RequestBody Account input) {
//        Account account = accountService.getByIdentifier(input.getIdentifier());
//        if (account == null) {
//            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
//        }
//        String salt = account.getSalt();
//        String dbPassword = account.getPassword();
//        String inputPassword = input.getPassword();
//        Boolean result = MD5Util.MD5(inputPassword + salt).equals(dbPassword);
//        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
//    }
//
//
//    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
//    public ResponseEntity<AccountDto> getAccountById(@PathVariable(value = "id") int id) {
//        return new ResponseEntity<AccountDto>(AccountDto.FromAccount(accountService.get(id)), HttpStatus.OK);
//    }

    private AccountObject entityToObject(AccountEntity entity) {
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
