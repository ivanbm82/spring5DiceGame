package com.dicerollgame2.dicerollgame.auth;

import com.dicerollgame2.dicerollgame.security.auth.AuthenticationRequest;
import com.dicerollgame2.dicerollgame.security.auth.AuthenticationResponse;
import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.repository.PlayerRepository;
import com.dicerollgame2.dicerollgame.security.auth.AuthenticationService;
import com.dicerollgame2.dicerollgame.security.auth.RegisterRequest;
import com.dicerollgame2.dicerollgame.security.config.JwtService;
import com.dicerollgame2.dicerollgame.security.exception.ExceptionSecurity;
import com.dicerollgame2.dicerollgame.security.user.Role;
import com.dicerollgame2.dicerollgame.security.user.User;
import com.dicerollgame2.dicerollgame.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void register_EmailAlreadyExists_ThrowsEmailAlreadyExistsException() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstname("John");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password");

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(ExceptionSecurity.EmailAlreadyExistsException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any(User.class));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void authenticate_ValidAuthenticationRequest_ReturnsAuthenticationResponse() {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("john@example.com");
        authenticationRequest.setPassword("password");

        User authenticatedUser = User.builder()
                .firstname("John")
                .email(authenticationRequest.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(authenticatedUser));
        when(jwtService.generateToken(authenticatedUser)).thenReturn("jwtToken");

        // Act
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        // Assert
        assertThat(authenticationResponse).isNotNull();
        assertThat(authenticationResponse.getToken()).isEqualTo("jwtToken");
    }

    @Test
    public void authenticate_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("john@example.com");
        authenticationRequest.setPassword("password");

        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.authenticate(authenticationRequest))
                .isInstanceOf(ExceptionSecurity.UserNotFoundException.class)
                .hasMessage("User not found");
    }
}
