package com.falizsh.finance.identity.users.userEmail.repository;

import com.falizsh.finance.identity.users.userEmail.model.UserEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEmailQuery {

    Page<UserEmail> findAllByUserId(Long userId, Pageable pageable);

    Page<UserEmail> findAll(Pageable pageable);

}
