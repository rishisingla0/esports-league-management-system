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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${ORGANIZER_INVITE_CODE:}")
private String organizerInviteCode = "";

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }
        String role = request.role() == null || request.role().isBlank()
        ? "TEAM_MANAGER"
        : request.role().trim().toUpperCase(Locale.ROOT);

if (!role.equals("TEAM_MANAGER") && !role.equals("ORGANIZER")) {
    throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Invalid account type"
    );
}

if (role.equals("ORGANIZER") && !isValidOrganizerInviteCode(request.inviteCode())) {
    throw new ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "A valid Organizer invitation code is required"
    );
}

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

// Organizer registration is allowed only after invitation validation.
user.setRole(role);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return buildAuthResponse(savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.email().trim().toLowerCase();

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
    private boolean isValidOrganizerInviteCode(String suppliedCode) {

    if (organizerInviteCode == null
            || organizerInviteCode.isBlank()
            || suppliedCode == null
            || suppliedCode.isBlank()) {
        return false;
    }

    return MessageDigest.isEqual(
            organizerInviteCode.getBytes(StandardCharsets.UTF_8),
            suppliedCode.trim().getBytes(StandardCharsets.UTF_8)
    );
}
}