package com.falizsh.finance.identity.users.user.usecase;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.repository.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserByIdUseCase {

    private final UserQuery query;

    public User execute(Long userId) {
        return query.findById(userId);
    }

}
