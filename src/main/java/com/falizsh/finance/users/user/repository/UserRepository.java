package com.falizsh.finance.users.user.repository;

import com.falizsh.finance.users.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@NotBlank @Email String email);

    User findByEmail(String username);
}
