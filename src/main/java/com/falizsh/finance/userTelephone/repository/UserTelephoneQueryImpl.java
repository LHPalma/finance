package com.falizsh.finance.userTelephone.repository;

import com.falizsh.finance.userTelephone.model.UserTelephone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserTelephoneQueryImpl implements UserTelephoneQuery {

    private final UserTelephoneRepository repositoy;

    @Override
    public Page<UserTelephone> findByUserId(Long userId, Pageable pageable) {
        return repositoy.findByUserId(userId, pageable);
    }

    @Override
    public Page<UserTelephone> findByAreaCode(String areaCode, Pageable pageable) {
        return repositoy.findByAreaCode(areaCode, pageable);
    }
    
}
