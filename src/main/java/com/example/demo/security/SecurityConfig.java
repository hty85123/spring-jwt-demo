package com.example.demo.security;


import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SecurityConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;  // Inject the repository manually
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationFilter jwtAuthFilter
    ) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                    // Permit all users to access registration and login
                    .requestMatchers(HttpMethod.POST, "/users", "/auth/login").permitAll()
                    // Authenticated users can access their own info
                    .requestMatchers(HttpMethod.GET, "/me").authenticated()
                    // Only ADMIN can delete users
                    .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAuthority("ADMIN")
                    // Only authenticated users can get all members, optionally restrict further
                    .requestMatchers(HttpMethod.GET, "/users").authenticated()
                )
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .build();
    }

    // Define the UserDetailsService Bean manually and pass the repository
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(memberRepository);
    }

    // Define Password Encoder Bean (NoOp for simplicity)
    // Have to change for production environment
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public JwtService jwtService(
            @Value("${jwt.secret-key}") String secretKeyStr,
            @Value("${jwt.valid-seconds}") int validSeconds
    ) {
        return new JwtService(secretKeyStr, validSeconds);
    }
}