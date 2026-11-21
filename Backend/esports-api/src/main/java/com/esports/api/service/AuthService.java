
package com.esports.api.service;

import com.esports.api.dto.AuthResponse;
import com.esports.api.dto.LoginRequest;
import com.esports.api.dto.RegisterRequest;
import com.esports.api.entity.User;
import com.esports.api.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }

        String role = request.role() == null || request.role().isBlank()
                ? "TEAM_MANAGER"
                : request.role().trim().toUpperCase(Locale.ROOT);

        // Organizer accounts must be created administratively.
        Set<String> allowedRoles = Set.of(
                "TEAM_MANAGER",
                "PLAYER",
                "VIEWER"
        );

        if (!allowedRoles.contains(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Organizer accounts must be provisioned by an administrator"
            );
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(role);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return buildAuthResponse(savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.email().trim().toLowerCase(Locale.ROOT);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                request.password()
                        )
                );

        User user = userRepository.findByEmail(
                authentication.getName()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
        ));

        String requestedRole = request.role() == null
                ? ""
                : request.role().trim();

        if (!user.getRole().equalsIgnoreCase(requestedRole)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email, password, or account type"
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        return buildAuthResponse(user, token);
    }

    private AuthResponse buildAuthResponse(User user, String token) {

        return new AuthResponse(
                token,
                "Bearer",
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
