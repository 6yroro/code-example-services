package com.test.security;

import com.test.services.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexander Zubkov
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class SecurityTest {

    @MockBean
    private TokenService tokenService;
    @MockBean
    private TokenConfig tokenConfig;

    @Autowired
    private MockMvc mvc;

    private final String auditPath, dataPath, authPath, registerPath;
    private final String header, prefix, secret, token, headerValue;

    private static final String USER = "user";
    private static final String USER_AUTH = "USER";
    private static final String AUDIT_AUTH = "AUDIT";
    private static final String ADMIN_AUTH = "ADMIN";

    public SecurityTest() {
        auditPath = "/audit";
        dataPath = "/data";
        authPath = "/auth";
        registerPath = "/auth/register";
        header = "Authorization";
        prefix = "Bearer";
        secret = "SECRET";
        token = "token";
        headerValue = prefix + token;
    }

    @Before
    public void setUp() {
        when(tokenConfig.getHeader()).thenReturn(header);
    }

    @Test
    public void getAuthWithoutAuthority() throws Exception {
        mvc.perform(post(authPath))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER, authorities = {USER_AUTH})
    public void getAuthWithUserAuthority() throws Exception {
        mvc.perform(post(authPath))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER, authorities = {AUDIT_AUTH})
    public void getAuthWithAuditAuthority() throws Exception {
        mvc.perform(post(authPath))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER, authorities = {ADMIN_AUTH})
    public void getAuthWithAdminAuthority() throws Exception {
        mvc.perform(post(authPath))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getRegisterWithoutAuthority() throws Exception {
        mvc.perform(post(registerPath))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER, authorities = {USER_AUTH})
    public void getRegisterWithUserAuthority() throws Exception {
        mvc.perform(post(registerPath))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USER, authorities = {AUDIT_AUTH})
    public void getRegisterWithAuditAuthority() throws Exception {
        mvc.perform(post(registerPath))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USER, authorities = {ADMIN_AUTH})
    public void getRegisterWithAdminAuthority() throws Exception {
        mvc.perform(post(registerPath))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDataWithoutAuthority() throws Exception {
        mvc.perform(get(dataPath))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER, authorities = {USER_AUTH})
    public void getDataWithUserAuthority() throws Exception {
        mvc.perform(get(dataPath))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER, authorities = {AUDIT_AUTH})
    public void getDataWithAuditAuthority() throws Exception {
        mvc.perform(get(dataPath))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER, authorities = {ADMIN_AUTH})
    public void getDataWithAdminAuthority() throws Exception {
        mvc.perform(get(dataPath))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAuditWithoutAuthority() throws Exception {
        mvc.perform(get(auditPath))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER, authorities = {USER_AUTH})
    public void getAuditWithUserAuthority() throws Exception {
        mvc.perform(get(auditPath))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USER, authorities = {AUDIT_AUTH})
    public void getAuditWithAuditAuthority() throws Exception {
        mvc.perform(get(auditPath))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER, authorities = {ADMIN_AUTH})
    public void getAuditWithAdminAuthority() throws Exception {
        mvc.perform(get(auditPath))
                .andExpect(status().isNotFound());
    }

    @Test
    public void authenticationTokenFilterNullUsername() throws Exception {
        when(tokenConfig.getPrefix()).thenReturn(prefix);
        when(tokenConfig.getSecret()).thenReturn(secret);
        mvc.perform(get(authPath).header(header, headerValue)).andReturn();
        verify(tokenService).validate(eq(token), eq(secret));
        verify(tokenService).getUsername();
        verify(tokenService, never()).getAuthorities();
    }

    @Test
    public void authenticationTokenFilterUsername() throws Exception {
        when(tokenConfig.getPrefix()).thenReturn(prefix);
        when(tokenConfig.getSecret()).thenReturn(secret);
        when(tokenService.getUsername()).thenReturn(USER);
        mvc.perform(get(authPath).header(header, headerValue)).andReturn();
        verify(tokenService).validate(eq(token), eq(secret));
        verify(tokenService).getUsername();
        verify(tokenService).getAuthorities();
    }

}