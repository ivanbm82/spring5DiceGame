package com.dicerollgame2.dicerollgame.security.auth;

import com.dicerollgame2.dicerollgame.model.domain.Player;
import com.dicerollgame2.dicerollgame.model.repository.PlayerRepository;
import com.dicerollgame2.dicerollgame.security.config.JwtService;
import com.dicerollgame2.dicerollgame.security.exception.ExceptionSecurity;
import com.dicerollgame2.dicerollgame.security.user.Role;
import com.dicerollgame2.dicerollgame.security.user.User;
import com.dicerollgame2.dicerollgame.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PlayerRepository playerRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        var email = request.getEmail();
        if (repository.findByEmail(email).isPresent()) {
            throw new ExceptionSecurity.EmailAlreadyExistsException("Email already exists");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        // Create the player
        Player player = new Player();
        player.setName(request.getFirstname());
        player.setRegistrationDate(new Date());
        player.setUser(user);
        playerRepository.save(player);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ExceptionSecurity.UserNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
