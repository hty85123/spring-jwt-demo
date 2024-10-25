package com.example.demo.security;

import com.example.demo.model.MemberAuthority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Get the Authorization header
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from header
        var jwt = authHeader.substring(BEARER_PREFIX.length());
        Claims claims;
        try {
            claims = jwtService.parseToken(jwt);
        } catch (ExpiredJwtException e) {
            // Handle expired token - set HTTP status and return the error message in the response body
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"JWT token is expired\"}");
            return;
        } catch (JwtException e) {
            // Handle invalid token - set HTTP status and return a generic error message
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
            return;
        }

        // Build UserDetails object from the claims
        var userDetails = new MemberUserDetails();
        userDetails.setId(claims.getSubject());
        userDetails.setUsername(claims.get("username", String.class));
        userDetails.setNickname(claims.get("nickname", String.class));

        var memberAuthorities = ((List<String>) claims.get("authorities"))
                .stream()
                .map(MemberAuthority::valueOf)
                .toList();
        userDetails.setMemberAuthorities(memberAuthorities);

        // Put the data into Security Context
        var token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(token);

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}
