package com.mh.auth.service.service;


import com.mh.auth.service.model.RefreshToken;
import com.mh.auth.service.model.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken validateRefreshToken(String token);

    void deleteAllUserTokens(User user);

    void deleteExpiredTokens();
}
