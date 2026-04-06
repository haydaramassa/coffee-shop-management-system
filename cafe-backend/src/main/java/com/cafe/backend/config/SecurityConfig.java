package com.cafe.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth

                        // public endpoints
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/forgot-password",
                                "/api/users/verify-reset-data"
                        ).permitAll()

                        // authenticated user endpoints
                        .requestMatchers("/api/users/by-username").authenticated()

                        // admin-only user management
                        .requestMatchers("/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/users/admin-create").hasRole("ADMIN")
                        .requestMatchers("/api/users/*/role").hasRole("ADMIN")
                        .requestMatchers("/api/users/*/enabled").hasRole("ADMIN")

                        // dashboard - admin only
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")

                        // categories - admin only
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")

                        // uploads - admin only
                        .requestMatchers("/api/uploads/**").hasRole("ADMIN")

                        // products
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "CASHIER")
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // customers
                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "CASHIER")

                        // orders
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("ADMIN", "CASHIER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/*/receipt").hasAnyRole("ADMIN", "CASHIER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/archive/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}