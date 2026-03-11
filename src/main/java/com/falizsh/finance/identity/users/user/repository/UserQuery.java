package com.falizsh.finance.identity.users.user.repository;

import com.falizsh.finance.identity.users.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserQuery {

    User findById(Long id);

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);

}
