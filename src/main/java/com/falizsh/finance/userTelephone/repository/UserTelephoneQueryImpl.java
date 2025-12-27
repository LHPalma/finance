package com.falizsh.finance.userTelephone.repository;

import com.falizsh.finance.userTelephone.dto.UserTelephoneCreateRequest;
import com.falizsh.finance.userTelephone.model.UserTelephone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserTelephoneQueryImpl implements UserTelephoneQuery {

    private final UserTelephoneRepository repository;;

    @Override
    public Page<UserTelephone> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<UserTelephone> findByUserId(Long userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    @Override
    public Page<UserTelephone> findByAreaCode(String areaCode, Pageable pageable) {
        return repository.findByAreaCode(areaCode, pageable);
    }


}
