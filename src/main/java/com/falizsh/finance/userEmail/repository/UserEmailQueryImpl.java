package com.falizsh.finance.userEmail.repository;

import com.falizsh.finance.userEmail.model.UserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

}
