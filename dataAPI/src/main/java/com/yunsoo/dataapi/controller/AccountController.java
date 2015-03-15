package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.AccountDto;
import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.AccountService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.Account;
import com.yunsoo.service.contract.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by KB on 3/13/2015.
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private final AccountService accountService;

    @Autowired
    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<AccountDto>(AccountDto.FromAccount(accountService.get(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/username/{username}",method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccountByUsername(@PathVariable(value = "username") String username) {
        return new ResponseEntity<AccountDto>(AccountDto.FromAccount(accountService.get(username)), HttpStatus.OK);
    }

    @RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccountByToken(@PathVariable(value = "token") String token) {
        return new ResponseEntity<AccountDto>(AccountDto.FromAccount(accountService.getByToken(token)), HttpStatus.OK);
    }

    public boolean verify(String username, String password) {
        return false;
    }

    public String getToken() {
        return "";
    }
}
