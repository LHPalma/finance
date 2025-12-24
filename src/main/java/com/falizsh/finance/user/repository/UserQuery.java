package com.falizsh.finance.user.repository;

import com.falizsh.finance.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuery {

    User findById(Long id);

    Page<User> findAll(Pageable pageable);

}
