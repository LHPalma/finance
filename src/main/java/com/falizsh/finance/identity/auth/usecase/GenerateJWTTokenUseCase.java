package com.falizsh.finance.identity.auth.usecase;

import com.falizsh.finance.identity.users.user.model.User;

public interface GenerateJWTTokenUseCase {

    String generate(User user);

}
