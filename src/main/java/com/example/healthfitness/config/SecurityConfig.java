package com.example.healthfitness.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService uds) {
        this.userDetailsService = uds;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**","/js/**","/img/**","/login","/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/meal-plans/**", "/meals/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/meals/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")     
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)    
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout"))
            .csrf(Customizer.withDefaults())
            .headers(h -> h
                .contentTypeOptions(Customizer.withDefaults())
                .frameOptions(f -> f.deny())
                .referrerPolicy(r -> r.policy(ReferrerPolicy.SAME_ORIGIN))
                .xssProtection(Customizer.withDefaults())
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true))
            )
            .addFilterAfter(cspHeaderFilter(), HeaderWriterFilter.class)
            .authenticationProvider(authProvider()); 

        return http.build();
    }

    @Bean
    public Filter cspHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                    throws ServletException, IOException {
                res.setHeader("Content-Security-Policy",
                    "default-src 'self'; " +
                    "script-src 'self' https://cdn.jsdelivr.net https://cdn.jsdelivr.net/npm; " +
                    "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdn.jsdelivr.net/npm; " +
                    "img-src 'self' data:; connect-src 'self'; frame-ancestors 'none'");
                res.setHeader("X-Content-Type-Options", "nosniff");
                res.setHeader("X-Frame-Options", "DENY");
                chain.doFilter(req, res);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService); // Twój DetailsService
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }
}


