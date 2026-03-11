package com.falizsh.finance.identity.users.user.repository;

import com.falizsh.finance.identity.users.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryImpl implements UserQuery {

    private final UserRepository repository;

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
