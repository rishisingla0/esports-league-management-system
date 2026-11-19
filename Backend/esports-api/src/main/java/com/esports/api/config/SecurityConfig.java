
package com.esports.api.config;

import com.esports.api.service.CustomUserDetailsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.cors.allowed-origin:http://localhost:3000}")
    private String frontendOrigin;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
        JwtAuthenticationFilter filter) {

    FilterRegistrationBean<JwtAuthenticationFilter> registration =
            new FilterRegistrationBean<>(filter);

    registration.setEnabled(false);

    return registration;
}

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(frontendOrigin));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

            http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                    )
                )

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                // Public endpoints
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/health"
                ).permitAll()

                // Current user
                .requestMatchers("/api/auth/me")
                    .authenticated()

                // Manager workspace endpoints
.requestMatchers("/api/manager/workspace/**")
    .hasRole("TEAM_MANAGER")


                // Public browsing
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/games/**",
                    "/api/organizations/**",
                    "/api/tournaments/**",
                    "/api/teams/**",
                    "/api/players/**",
                    "/api/coaches/**",
                    "/api/leaderboards/**",
                    "/api/analytics/**",
                    "/api/matches/**",
                    "/api/match-results/**",
                     "/api/dashboard/summary"
                ).permitAll()

                // Team Manager operations
                .requestMatchers("/api/onboarding/**")
                    .hasRole("TEAM_MANAGER")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/teams/**"
                ).hasRole("TEAM_MANAGER")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/teams/**"
                ).hasRole("TEAM_MANAGER")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/teams/**"
                ).hasRole("TEAM_MANAGER")

                // Organizer tournament operations
.requestMatchers(
    HttpMethod.POST,
    "/api/tournaments/**"
).hasRole("ORGANIZER")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/tournaments/**"
                ).hasRole("ORGANIZER")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/tournaments/**"
                ).hasRole("ORGANIZER")
                // Organizer registration management
.requestMatchers(
    HttpMethod.GET,
    "/api/tournament-registrations/**"
).hasRole("ORGANIZER")

.requestMatchers(
    HttpMethod.PUT,
    "/api/tournament-registrations/**"
).hasRole("ORGANIZER")

                // Everything else requires an explicit rule
                .anyRequest().denyAll()
            )

            .authenticationProvider(authenticationProvider())

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
