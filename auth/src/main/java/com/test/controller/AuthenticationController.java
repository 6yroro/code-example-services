package com.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Alexander Zubkov
 */
@RestController
public class AuthenticationController {

    @RequestMapping("/")
    public @ResponseBody ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    @RequestMapping("actuator/info")
    public @ResponseBody void actuator(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> authenticate(@RequestBody String authenticationRequest) {
        return ResponseEntity.ok(authenticationRequest);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity register(@RequestBody String authenticationRequest) {
        return new ResponseEntity<>(authenticationRequest, HttpStatus.CREATED);
    }

}
