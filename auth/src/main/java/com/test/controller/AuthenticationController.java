package com.test.controller;

import com.test.model.AuthenticationRequest;
import com.test.model.AuthenticationResponse;
import com.test.model.AuthUser;
import com.test.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alexander Zubkov
 */
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<AuthUser> register(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.registerUser(request), HttpStatus.CREATED);
    }

}
