package com.yunsoo.api.controller;

import com.yunsoo.api.object.TAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Zhe on 2015/3/5.
 * //Wait for Kaibing to implements it.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> grantRole(@RequestBody TAccount account) {
        if (account == null) {
            return new ResponseEntity<String>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //to-do

        //Fake token.
        return new ResponseEntity<String>("abjksad8230.LK76-7J32-KLS34-7fDLK-09SDF", HttpStatus.OK);
    }
}
