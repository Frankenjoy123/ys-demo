package com.yunsoo.data.api.controller;

import com.yunsoo.data.api.dto.ResultWrapper;
import com.yunsoo.data.api.factory.ResultFactory;
import com.yunsoo.data.service.service.AccountTokenService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.contract.AccountToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jerry on 3/15/2015.
 */
@RestController
@RequestMapping("/accountToken")
public class AccountTokenController {

    @Autowired
    private final AccountTokenService accountTokenService;

    @Autowired
    AccountTokenController(AccountTokenService accountTokenService) {
        this.accountTokenService = accountTokenService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<AccountToken> getAccountTokenById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<AccountToken>(accountTokenService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/identifier/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<AccountToken> getAccountTokenByIdentifier(@PathVariable(value = "identifier")String identifier) {
        return new ResponseEntity<AccountToken>(accountTokenService.getByIdentifier(identifier), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createAccountToken(@RequestBody AccountToken accountToken) {
        long id = accountTokenService.save(accountToken);
        HttpStatus status = id > 0 ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ResultWrapper> updateAccountToken(@RequestBody AccountToken accountToken) {
        Boolean result = accountTokenService.update(accountToken).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteAccountToken(@PathVariable(value = "id") int id) {
        Boolean result = accountTokenService.delete(id, 5); //deletePermanantly status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}