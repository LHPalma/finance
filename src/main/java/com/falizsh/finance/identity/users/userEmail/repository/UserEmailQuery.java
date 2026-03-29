package com.falizsh.finance.identity.users.userEmail.repository;

import com.falizsh.finance.identity.users.userEmail.model.EmailStatus;
import com.falizsh.finance.identity.users.userEmail.model.UserEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserEmailQuery {

    Page<UserEmail> findAllByUserId(Long userId, Pageable pageable);

    Page<UserEmail> findAll(Pageable pageable);

    Page<UserEmail> findEmailsByUserIdAndStatuses(Long userId, Set<EmailStatus> statuses, Pageable pageable);

    Page<UserEmail> findEmailsByUserId(Long userId, Pageable pageable);
}
