package com.mh.auth.service.service.impl;

import com.mh.auth.service.exception.BadRequestException;
import com.mh.auth.service.model.RefreshToken;
import com.mh.auth.service.model.User;
import com.mh.auth.service.repository.RefreshTokenRepository;
import com.mh.auth.service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    /**
     * Create a new refresh token for the user.
     * Deletes any existing tokens before creating new one.
     *
     * @param user the user for whom to create the token
     * @return newly created refresh token
     */
    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Validate refresh token existence and expiration.
     *
     * @param token refresh token string
     * @return valid refresh token entity
     * @throws BadRequestException if token is invalid or expired
     */
    @Override
    @Transactional
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token has expired. Please login again.");
        }

        return refreshToken;
    }

    /**
     * Delete all refresh tokens for a specific user.
     *
     * @param user the user whose tokens should be deleted
     */
    @Override
    @Transactional
    public void deleteAllUserTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
        log.info("All refresh tokens deleted for user: {}", user.getEmail());
    }

    /**
     * Delete all expired refresh tokens from the database.
     * Should be called periodically by a scheduled task.
     */
    @Override
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(Instant.now());
        log.info("Expired refresh tokens cleaned up");
    }
}