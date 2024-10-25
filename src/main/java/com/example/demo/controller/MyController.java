package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.JwtService;
import com.example.demo.security.MemberUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MyController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;

    // Handle exceptions and return unified error response
    private ResponseEntity<?> handleException(Exception e, HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message + ": " + e.getMessage());
    }

    // Get All Users API
    @GetMapping("/users")
    public ResponseEntity<?> getAllMembers() {
        try {
            List<Member> members = memberRepository.findAll();
            List<MemberSummary> memberSummaries = members.stream()
                    .map(member -> new MemberSummary(member.getId(), member.getUsername(), member.getNickname()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(memberSummaries);
        } catch (Exception e) {
            return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching users");
        }
    }

    // Create User API
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            // Validate non-empty fields
            if (request.getUsername() == null || request.getUsername().isEmpty() ||
                    request.getPassword() == null || request.getPassword().isEmpty() ||
                    request.getNickname() == null || request.getNickname().isEmpty() ||
                    request.getAuthorities() == null || request.getAuthorities().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields (username, password, nickname, authorities) must be provided and non-empty");
            }

            // Check if the user already exists
            Optional<Member> existingMember = memberRepository.findByUsername(request.getUsername());
            if (existingMember.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            // Validate authorities by converting the string to MemberAuthority enum
            List<MemberAuthority> validAuthorities;
            try {
                validAuthorities = request.getAuthorities().stream()
                        .map(auth -> Enum.valueOf(MemberAuthority.class, auth)) // Convert from String to Enum
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authority value provided.");
            }

            // Create new user instance
            Member newUser = new Member();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(request.getPassword()); // In production, hash the password
            newUser.setNickname(request.getNickname());
            newUser.setAuthorities(validAuthorities); // Use the validated authorities

            // Save the instance to the database
            Member savedUser = memberRepository.save(newUser);

            CreateUserResponse response = new CreateUserResponse(
                    savedUser.getId(), savedUser.getUsername(), "User created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user");
        }
    }

    // Login API
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            var auth = authenticationManager.authenticate(token);
            var user = (MemberUserDetails) auth.getPrincipal();
            var jwt = jwtService.createLoginAccessToken(user);

            return ResponseEntity.ok(LoginResponse.of(jwt, user));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error during login");
        }
    }

    // Delete User API
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            Optional<Member> existingMember = memberRepository.findById(id);
            if (existingMember.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            memberRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user");
        }
    }

    // Current User Info API
    @GetMapping("/me")
    public ResponseEntity<?> home() {
        try {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if ("anonymousUser".equals(principal)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not valid");
            }

            var userDetails = (MemberUserDetails) principal;
            return ResponseEntity.ok(String.format("Your ID: %s%nAccount：%s%nNickName：%s%nAuthority：%s",
                    userDetails.getId(), userDetails.getUsername(), userDetails.getNickname(), userDetails.getAuthorities()));
        } catch (Exception e) {
            return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching home info");
        }
    }
}