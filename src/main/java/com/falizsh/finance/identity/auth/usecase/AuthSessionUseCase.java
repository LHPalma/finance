package com.falizsh.finance.identity.auth.usecase;

import com.falizsh.finance.identity.auth.dto.response.AuthResponse;
import com.falizsh.finance.identity.users.user.model.User;

public interface AuthSessionUseCase {

    AuthResponse createSession(User user);

    AuthResponse refreshSession(String refreshToken);

}
