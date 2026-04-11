package com.falizsh.finance.identity.auth.application.usecase;

import com.falizsh.finance.identity.auth.application.dto.response.AuthResponse;
import com.falizsh.finance.identity.users.user.model.User;

public interface AuthSessionUseCase {

    AuthResponse createSession(User user);

    AuthResponse refreshSession(String refreshToken);

}
