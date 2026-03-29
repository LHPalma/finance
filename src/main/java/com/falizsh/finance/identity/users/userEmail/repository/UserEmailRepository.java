package com.falizsh.finance.identity.users.userEmail.repository;

import com.falizsh.finance.identity.users.userEmail.model.EmailStatus;
import com.falizsh.finance.identity.users.userEmail.model.UserEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserEmailRepository
        extends JpaRepository<UserEmail, Long> {

    List<UserEmail> findAllByUserId(Long userId);

    Page<UserEmail> findAllByUserId(Long userId, Pageable pageable);

    Page<UserEmail> findAllByUserIdAndStatusIn(Long userId, Set<EmailStatus> statuses, Pageable pageable);

}
