package com.example.auth_service.common.config;

import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> !header.isBlank())
                .map(header -> header.substring(7))
                .map(jwtService::extractUserId)
                .flatMap(userId -> userRepository.findById(Long.valueOf(userId)))
                .ifPresent(userDetails -> {
                    request.setAttribute("X-User-Id", userDetails.getId());
                    processAuthentication(request, userDetails);
                });
        filterChain.doFilter(request, response);
    }

    private void processAuthentication(final HttpServletRequest request, final UserDetails userDetails) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        Optional.of(jwtToken)
                .filter(token -> !jwtService.isExpired(token))
                .ifPresent(token -> {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
    }
}
