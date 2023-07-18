package com.dicerollgame2.dicerollgame.controller;


import com.dicerollgame2.dicerollgame.security.auth.AuthenticationRequest;
import com.dicerollgame2.dicerollgame.security.auth.AuthenticationResponse;
import com.dicerollgame2.dicerollgame.security.auth.AuthenticationService;
import com.dicerollgame2.dicerollgame.security.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/players/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    // Registers a new user
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody @Valid RegisterRequest request) {
        AuthenticationResponse response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody @Valid AuthenticationRequest request) {
        try {
            // Authenticates a user
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Returns UNAUTHORIZED status if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
