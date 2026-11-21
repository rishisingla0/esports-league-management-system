
package com.esports.api.config;

import com.esports.api.entity.User;
import com.esports.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    name = "app.bootstrap-organizer.enabled",
    havingValue = "true"
)
public class OrganizerAccountBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap-organizer.full-name:}")
    private String fullName;

    @Value("${app.bootstrap-organizer.email:}")
    private String email;

    @Value("${app.bootstrap-organizer.password:}")
    private String password;

    public OrganizerAccountBootstrap(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (fullName.isBlank() || email.isBlank()
                || password.isBlank() || password.length() < 8
                || password.length() > 72) {
            throw new IllegalStateException(
                "Valid Organizer bootstrap credentials are required"
            );
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            System.out.println(
                "Organizer account already exists. Skipping creation."
            );
            return;
        }

        User organizer = new User();
        organizer.setFullName(fullName.trim());
        organizer.setEmail(normalizedEmail);
        organizer.setPasswordHash(
            passwordEncoder.encode(password)
        );
        organizer.setRole("ORGANIZER");
        organizer.setEnabled(true);

        userRepository.save(organizer);

        System.out.println(
            "Organizer account successfully created: " + normalizedEmail
        );
    }
}
