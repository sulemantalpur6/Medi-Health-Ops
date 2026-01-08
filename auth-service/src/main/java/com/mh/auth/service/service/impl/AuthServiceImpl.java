package com.mh.auth.service.service.impl;

import com.mh.auth.service.constant.RoleEnum;
import com.mh.auth.service.dto.request.ChangePasswordRequest;
import com.mh.auth.service.dto.request.LoginRequest;
import com.mh.auth.service.dto.request.RefreshTokenRequest;
import com.mh.auth.service.dto.request.SignupRequest;
import com.mh.auth.service.dto.respone.AuthResponse;
import com.mh.auth.service.dto.respone.MessageResponse;
import com.mh.auth.service.dto.respone.UserInfoResponse;
import com.mh.auth.service.event.UserCreatedEvent;
import com.mh.auth.service.exception.BadRequestException;
import com.mh.auth.service.exception.ResourceNotFoundException;
import com.mh.auth.service.mapper.UserMapper;
import com.mh.auth.service.model.RefreshToken;
import com.mh.auth.service.model.Role;
import com.mh.auth.service.model.User;
import com.mh.auth.service.repository.RoleRepository;
import com.mh.auth.service.repository.UserRepository;
import com.mh.auth.service.service.AuthService;
import com.mh.auth.service.service.RefreshTokenService;
import com.mh.auth.service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Register a new user in the system.
     * Creates user with assigned role and publishes event for user profile creation.
     *
     * @param request signup details including email, password, and role
     * @return authentication response with access and refresh tokens
     * @throws BadRequestException if email already exists or role is invalid
     */
    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        log.info("Signup request received for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new BadRequestException("Email is already registered!");
        }

        User user = buildNewUser(request);
        assignRoleToUser(user, request.getRole());
        user = userRepository.save(user);

        publishUserCreatedEvent(user, request);
        log.info("User created successfully with ID: {}", user.getId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return generateAuthResponse(userDetails, user);
    }

    /**
     * Authenticate user and generate access tokens.
     * Handles failed login attempts and account locking.
     *
     * @param request login credentials
     * @return authentication response with tokens
     * @throws BadRequestException if credentials are invalid or account is locked
     */
    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login request received for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        validateAccountStatus(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            handleSuccessfulLogin(user);
            log.info("User logged in successfully: {}", user.getEmail());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return generateAuthResponse(userDetails, user);

        } catch (Exception e) {
            handleFailedLogin(user);
            throw new BadRequestException("Invalid email or password");
        }
    }

    /**
     * Generate new access token using refresh token.
     *
     * @param request refresh token request
     * @return new authentication response with tokens
     * @throws BadRequestException if refresh token is invalid or expired
     */
    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refresh token request received");

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        log.info("Refresh token validated successfully for user: {}", user.getEmail());
        return generateAuthResponse(userDetails, user);
    }

    /**
     * Logout user and invalidate all refresh tokens.
     *
     * @param email user email
     * @return success message
     * @throws ResourceNotFoundException if user not found
     */
    @Override
    @Transactional
    public MessageResponse logout(String email) {
        log.info("Logout request for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        refreshTokenService.deleteAllUserTokens(user);
        log.info("User logged out successfully: {}", email);

        return new MessageResponse("Logged out successfully");
    }

    /**
     * Change user password after verifying current password.
     * Invalidates all existing refresh tokens.
     *
     * @param email   user email
     * @param request password change details
     * @return success message
     * @throws BadRequestException if current password is incorrect or passwords don't match
     */
    @Override
    @Transactional
    public MessageResponse changePassword(String email, ChangePasswordRequest request) {
        log.info("Change password request for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validatePasswordChange(user, request);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        refreshTokenService.deleteAllUserTokens(user);
        log.info("Password changed successfully for user: {}", email);

        return new MessageResponse("Password changed successfully. Please login again.");
    }

    /**
     * Get current authenticated user information.
     *
     * @param email user email
     * @return user information with roles and permissions
     * @throws ResourceNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUser(String email) {
        log.info("Get current user request for: {}", email);

        User user = userRepository.findByEmailWithRolesAndPermissions(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toUserInfoResponse(user);
    }

    private User buildNewUser(SignupRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedLoginAttempts(0)
                .build();
    }

    private void assignRoleToUser(User user, String requestedRole) {
        String roleName = (requestedRole != null && !requestedRole.isEmpty())
                ? requestedRole
                : RoleEnum.ROLE_PATIENT.getName();

        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
    }

    private void publishUserCreatedEvent(User user, SignupRequest request) {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(user.getRoles().iterator().next().getName())
                .timestamp(Instant.now())
                .build();

        eventPublisher.publishEvent(event);
        log.info("UserCreatedEvent published for user ID: {}", user.getId());
    }

    private void validateAccountStatus(User user) {
        if (user.isAccountLocked()) {
            long minutesLeft = java.time.Duration.between(
                    LocalDateTime.now(),
                    user.getLockedUntil()
            ).toMinutes();

            throw new BadRequestException(
                    String.format("Account is locked. Try again in %d minutes.", minutesLeft)
            );
        }
    }

    private void handleSuccessfulLogin(User user) {
        user.resetFailedAttempts();
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private void handleFailedLogin(User user) {
        user.incrementFailedAttempts();

        if (user.getFailedLoginAttempts() >= 5) {
            user.lockAccount(15);
            log.warn("Account locked due to multiple failed login attempts: {}", user.getEmail());
        }

        userRepository.save(user);
    }

    private void validatePasswordChange(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
    }

    private AuthResponse generateAuthResponse(UserDetails userDetails, User user) {
        Map<String, Object> extraClaims = buildTokenClaims(userDetails);
        String accessToken = jwtUtil.generateToken(userDetails, user.getId(), extraClaims);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000)
                .user(userMapper.toUserInfoResponse(user))
                .build();
    }

    private Map<String, Object> buildTokenClaims(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .collect(Collectors.joining(","));

        String permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> !auth.startsWith("ROLE_"))
                .collect(Collectors.joining(","));

        claims.put("roles", roles);
        claims.put("permissions", permissions);

        return claims;
    }
}
