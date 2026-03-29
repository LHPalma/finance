package com.falizsh.finance.identity.users.userEmail.repository;

import com.falizsh.finance.identity.users.userEmail.model.EmailStatus;
import com.falizsh.finance.identity.users.userEmail.model.UserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserEmailQueryImpl implements UserEmailQuery {

    private final UserEmailRepository repository;

    public Page<UserEmail> findAllByUserId(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<UserEmail> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<UserEmail> findEmailsByUserIdAndStatuses(Long userId, Set<EmailStatus> statuses, Pageable pageable) {
        return repository.findAllByUserIdAndStatusIn(userId, statuses, pageable);
    }

    @Override
    public Page<UserEmail> findEmailsByUserId(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }


}
