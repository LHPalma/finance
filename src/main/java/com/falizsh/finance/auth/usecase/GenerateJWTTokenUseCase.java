package com.falizsh.finance.auth.usecase;

import com.falizsh.finance.users.user.model.User;

public interface GenerateJWTTokenUseCase {

    String generate(User user);

}
