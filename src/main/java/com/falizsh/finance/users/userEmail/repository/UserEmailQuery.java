package com.falizsh.finance.users.userEmail.repository;

import com.falizsh.finance.users.userEmail.model.UserEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEmailQuery {

    Page<UserEmail> findAllByUserId(Long userId, Pageable pageable);

    Page<UserEmail> findAll(Pageable pageable);

}
