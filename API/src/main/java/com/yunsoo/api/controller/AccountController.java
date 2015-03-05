package com.yunsoo.api.controller;

import com.yunsoo.api.data.DataAPIClient;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountRole;
import com.yunsoo.api.security.AccountAuthentication;
import org.eclipse.jetty.security.UserAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/3/5.
 * Handle with accounts which consumes this API.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private DataAPIClient dataAPIClient;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public TAccount getCurrent() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AccountAuthentication) {
            return ((AccountAuthentication) authentication).getDetails();
        }
        return new TAccount(authentication.getName()); //anonymous user support
    }

    @RequestMapping(value = "/current", method = RequestMethod.PATCH)
    public ResponseEntity<String> changePassword(@RequestBody final TAccount tAccount) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        final TAccount currentUser = userRepository.findByUsername(authentication.getName());
        final TAccount currentUser = new TAccount(authentication.getName()); //mock current User. To-do: implement it.

        if (tAccount.getNewPassword() == null || tAccount.getNewPassword().length() < 4) {
            return new ResponseEntity<String>("new password to short", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        if (!pwEncoder.matches(tAccount.getPassword(), currentUser.getPassword())) {
            return new ResponseEntity<String>("old password mismatch", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        currentUser.setPassword(pwEncoder.encode(tAccount.getNewPassword()));
        //To-do, persistent changes.
        //userRepository.saveAndFlush(currentUser);
        return new ResponseEntity<String>("password changed", HttpStatus.OK);
    }

    @RequestMapping(value = "/{account}/grant/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<String> grantRole(@PathVariable TAccount account, @PathVariable TAccountRole role) {
        if (account == null) {
            return new ResponseEntity<String>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        account.grantRole(role);
        //to-do
        //userRepository.saveAndFlush(tAccount);
        return new ResponseEntity<String>("role granted", HttpStatus.OK);
    }

    @RequestMapping(value = "/{account}/revoke/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<String> revokeRole(@PathVariable TAccount account, @PathVariable TAccountRole role) {
        if (account == null) {
            return new ResponseEntity<String>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        account.revokeRole(role);
        //to-do
//        userRepository.saveAndFlush(user);
        return new ResponseEntity<String>("role revoked", HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<TAccount> list() {
        //return userRepository.findAll();
        return null;
    }
}

