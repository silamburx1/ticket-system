package com.example.tickets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
            .authorizeHttpRequests(a -> a
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll())
            // 401 without WWW-Authenticate, so the browser doesn't show its own login popup
            .httpBasic(b -> b.authenticationEntryPoint((req, res, ex) -> res.sendError(401)));
        return http.build();
    }

    @Bean
    UserDetailsService users(@Value("${admin.username}") String u, @Value("${admin.password}") String p) {
        return new InMemoryUserDetailsManager(
            User.withUsername(u).password("{noop}" + p).roles("ADMIN").build());
    }
}
