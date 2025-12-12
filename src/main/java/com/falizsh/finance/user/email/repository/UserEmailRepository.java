package com.falizsh.finance.user.email.repository;

import com.falizsh.finance.user.email.model.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEmailRepository
        extends JpaRepository<UserEmail, Long> {

    List<UserEmail> findByUserId(Long userId);

    boolean existsByEmail(String email);

    Optional<UserEmail> findByUserIdAndIsPrimaryTrue(Long userId);
}
