package com.dicerollgame2.dicerollgame.controller;


import com.dicerollgame2.dicerollgame.security.auth.AuthenticationRequest;
import com.dicerollgame2.dicerollgame.security.auth.AuthenticationResponse;
import com.dicerollgame2.dicerollgame.security.auth.AuthenticationService;
import com.dicerollgame2.dicerollgame.security.auth.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_WithValidRequest_ReturnsCreatedResponse() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthenticationResponse("token"));

        // Act
        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo("token");
    }

    @Test
    void authenticateUser_WithValidRequest_ReturnsOkResponse() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse("token"));

        // Act
        ResponseEntity<AuthenticationResponse> response = (ResponseEntity<AuthenticationResponse>) authenticationController.authenticateUser(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo("token");
    }

    @Test
    void authenticateUser_WithInvalidRequest_ReturnsUnauthorizedResponse() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act
        ResponseEntity<AuthenticationResponse> response = (ResponseEntity<AuthenticationResponse>) authenticationController.authenticateUser(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
    }
}