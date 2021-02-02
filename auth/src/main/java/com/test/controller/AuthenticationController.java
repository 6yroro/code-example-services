package com.test.controller;

import com.test.database.entity.AuthUser;
import com.test.model.AuthenticationRequest;
import com.test.model.AuthenticationResponse;
import com.test.services.AuthenticationAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alexander Zubkov
 */
@RestController
public class AuthenticationController {

    private final AuthenticationAuditService authenticationAuditService;

    @Autowired
    public AuthenticationController(AuthenticationAuditService authenticationAuditService) {
        this.authenticationAuditService = authenticationAuditService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(new AuthenticationResponse(authenticationAuditService.authenticate(request)));
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuthUser> register(@RequestBody AuthenticationRequest request,
                                             @RequestHeader("Username") String username) {
        return new ResponseEntity<>(authenticationAuditService.registerUser(request, username), HttpStatus.CREATED);
    }

}
