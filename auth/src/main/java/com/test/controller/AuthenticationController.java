package com.test.controller;

import com.test.database.entity.AuthUser;
import com.test.model.AuthenticationRequest;
import com.test.model.AuthenticationResponse;
import com.test.services.AuthUserAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alexander Zubkov
 */
@RestController
public class AuthenticationController {

    private final AuthUserAuditService authUserAuditService;

    @Autowired
    public AuthenticationController(AuthUserAuditService authUserAuditService) {
        this.authUserAuditService = authUserAuditService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(new AuthenticationResponse(authUserAuditService.authenticate(request)));
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuthUser> register(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authUserAuditService.register(request), HttpStatus.CREATED);
    }

}
