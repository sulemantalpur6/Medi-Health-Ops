package com.mh.auth.service.service;

import com.mh.auth.service.dto.request.ChangePasswordRequest;
import com.mh.auth.service.dto.request.LoginRequest;
import com.mh.auth.service.dto.request.RefreshTokenRequest;
import com.mh.auth.service.dto.request.SignupRequest;
import com.mh.auth.service.dto.respone.AuthResponse;
import com.mh.auth.service.dto.respone.MessageResponse;
import com.mh.auth.service.dto.respone.UserInfoResponse;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    MessageResponse logout(String email);

    MessageResponse changePassword(String email, ChangePasswordRequest request);

    UserInfoResponse getCurrentUser(String email);
}