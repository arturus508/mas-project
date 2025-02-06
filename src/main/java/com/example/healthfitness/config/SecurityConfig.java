package com.example.healthfitness.config;

import com.example.healthfitness.service.DetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final DetailsService detailsService;

    public SecurityConfig(DetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // If you want to keep CSRF, remove ".ignoringRequestMatchers(...)"
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Anyone can access /login, /register, and static resources
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                        // Everything else requires login
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        // The login page is GET /login
                        .loginPage("/login")
                        // The form will POST to /login
                        .loginProcessingUrl("/login")
                        // The form fields are name="email" and name="password"
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // On success, go to /dashboard
                        .defaultSuccessUrl("/dashboard", true)
                        // On failure, append ?error
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        // Logging out is done at /logout
                        .logoutUrl("/logout")
                        // After logout, go back to /login?logout=true
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }
}





