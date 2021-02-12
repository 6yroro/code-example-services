package com.test.services;

import com.test.database.repository.AuthUserRepository;
import com.test.model.AuthenticationRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

/**
 * @author Alexander Zubkov
 */
@SpringBootTest(classes = AuthUserRegistrationValidationTestConfiguration.class)
@RunWith(SpringRunner.class)
public class AuthUserRegistrationValidationTest {

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AuthUserRepository authUserRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private TokenService tokenService;

    @Autowired
    private AuthUserService authUserService;

    private AuthenticationRequest request;
    private String correct, size0, size1, size2, size3, size100, size101, size200, size201;

    public AuthUserRegistrationValidationTest() {
        correct = "test";
        size0 = "";
        size1 = "1";
        size2 = "12";
        size3 = "123";
        size100 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        size101 = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901";
        size200 = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        size201 = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901";
    }

    @Before
    public void setUp() {
        request = new AuthenticationRequest();
    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyRequest() {
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void nullUsernameEmptyPassword() {
        request.setPassword(size0);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyUsernameNullPassword() {
        request.setUsername(size0);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyUsernameEmptyPassword() {
        request.setUsername(size0);
        request.setPassword(size0);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void usernameSize1() {
        request.setUsername(size1);
        request.setPassword(correct);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void usernameSize2() {
        request.setUsername(size2);
        request.setPassword(correct);
        authUserService.register(request);
    }

    @Test
    public void usernameSize3() {
        request.setUsername(size3);
        request.setPassword(correct);
        authUserService.register(request);
    }

    @Test
    public void usernameSize100() {
        request.setUsername(size100);
        request.setPassword(correct);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void usernameSize101() {
        request.setUsername(size101);
        request.setPassword(correct);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void passwordSize1() {
        request.setUsername(correct);
        request.setPassword(size1);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void passwordSize2() {
        request.setUsername(correct);
        request.setPassword(size2);
        authUserService.register(request);
    }

    @Test
    public void passwordSize3() {
        request.setUsername(correct);
        request.setPassword(size3);
        authUserService.register(request);
    }

    @Test
    public void passwordSize200() {
        request.setUsername(correct);
        request.setPassword(size200);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void passwordSize201() {
        request.setUsername(correct);
        request.setPassword(size201);
        authUserService.register(request);
    }

    @Test
    public void authoritySize100() {
        request.setUsername(correct);
        request.setPassword(correct);
        request.setAuthorities(size100);
        authUserService.register(request);
    }

    @Test(expected = ConstraintViolationException.class)
    public void authoritySize101() {
        request.setUsername(correct);
        request.setPassword(correct);
        request.setAuthorities(size101);
        authUserService.register(request);
    }

}