package com.falizsh.finance.user.repository;

import com.falizsh.finance.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
