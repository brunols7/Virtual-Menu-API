package com.brunols.virtual_menu;

import com.brunols.virtual_menu.controller.AuthenticationController;
import com.brunols.virtual_menu.dto.LoginResponse;
import com.brunols.virtual_menu.dto.LoginUserDto;
import com.brunols.virtual_menu.dto.RegisterUserDto;
import com.brunols.virtual_menu.entity.User;
import com.brunols.virtual_menu.infra.security.AuthenticationService;
import com.brunols.virtual_menu.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationService authenticationService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public AuthenticationService authenticationService() {
            return Mockito.mock(AuthenticationService.class);
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return Mockito.mock(UserDetailsService.class);
        }

        @Bean
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().authenticated()
                    );
            return http.build();
        }
    }

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private User user;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        registerUserDto = new RegisterUserDto("Test User", "test@example.com", "password123");
        loginUserDto = new LoginUserDto("test@example.com", "password123");
        user = new User();
        user.setId(1L);
        user.setFullName("Test User");
        user.setEmail("test@example.com");

        String fakeToken = "fake.jwt.token";
        loginResponse = new LoginResponse().setToken(fakeToken).setExpiresIn(3600L);
    }

    @Test
    @DisplayName("Should register a new user successfully and return 200 OK")
    void signup_whenGivenValidDto_shouldReturnCreatedUserAndOk() throws Exception {
        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("Should authenticate user successfully and return a JWT")
    void login_whenGivenValidCredentials_shouldReturnJwtAndOk() throws Exception {
        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(loginResponse.getToken());
        when(jwtService.getExpirationTime()).thenReturn(loginResponse.getExpiresIn());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(loginResponse.getToken()))
                .andExpect(jsonPath("$.expiresIn").value(loginResponse.getExpiresIn()));
    }

    @Test
    @DisplayName("Should fail authentication and return an error")
    void login_whenGivenInvalidCredentials_shouldReturnError() throws Exception {
        String errorMessage = "Invalid Credentials";
        when(authenticationService.authenticate(any(LoginUserDto.class)))
                .thenThrow(new BadCredentialsException(errorMessage));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isForbidden());
    }
}