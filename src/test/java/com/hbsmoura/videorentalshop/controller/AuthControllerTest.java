package com.hbsmoura.videorentalshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbsmoura.videorentalshop.config.security.FakeSecurityConfig;
import com.hbsmoura.videorentalshop.config.security.JwtAuthenticationFilter;
import com.hbsmoura.videorentalshop.dtos.UserLoginDto;
import com.hbsmoura.videorentalshop.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        value = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@Import(FakeSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Authenticate user test")
    @WithAnonymousUser
    void authenticateTest() throws Exception {
        UserLoginDto user = UserLoginDto.builder()
                .username("username")
                .password("password")
                .build();

        doReturn("Json.Web.Token").when(authService).authenticate(any(UserLoginDto.class));

        mockMvc
                .perform(
                        post("/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(user))

                )
                .andExpect(status().isOk());
    }
}
