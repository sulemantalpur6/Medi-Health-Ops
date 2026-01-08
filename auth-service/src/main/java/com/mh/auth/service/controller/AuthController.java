package com.mh.auth.service.controller;

import com.mh.auth.service.dto.request.ChangePasswordRequest;
import com.mh.auth.service.dto.request.LoginRequest;
import com.mh.auth.service.dto.request.RefreshTokenRequest;
import com.mh.auth.service.dto.request.SignupRequest;
import com.mh.auth.service.dto.respone.AuthResponse;
import com.mh.auth.service.dto.respone.MessageResponse;
import com.mh.auth.service.dto.respone.UserInfoResponse;
import com.mh.auth.service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Handles user registration, login, token refresh, and password management.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user.
     *
     * @param request signup details
     * @return authentication response with tokens
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        log.info("Signup endpoint called for email: {}", request.getEmail());
        AuthResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticate user and generate tokens.
     *
     * @param request login credentials
     * @return authentication response with tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login endpoint called for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh access token using refresh token.
     *
     * @param request refresh token
     * @return new authentication response with tokens
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token endpoint called");
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user and invalidate refresh tokens.
     *
     * @param authentication current authenticated user
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(Authentication authentication) {
        String email = authentication.getName();
        log.info("Logout endpoint called for user: {}", email);
        MessageResponse response = authService.logout(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Change user password.
     *
     * @param request password change details
     * @param authentication current authenticated user
     * @return success message
     */
    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        log.info("Change password endpoint called for user: {}", email);
        MessageResponse response = authService.changePassword(email, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get current authenticated user information.
     *
     * @param authentication current authenticated user
     * @return user information with roles and permissions
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        log.info("Get current user endpoint called for: {}", email);
        UserInfoResponse response = authService.getCurrentUser(email);
        return ResponseEntity.ok(response);
    }
}