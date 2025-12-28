package com.falizsh.finance.users.userTelephone.repository;

import com.falizsh.finance.users.userTelephone.model.UserTelephone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserTelephoneQuery {

    Page<UserTelephone> findAll(Pageable pageable);

    Page<UserTelephone> findByUserId(Long userId, Pageable pageable);

    Page<UserTelephone> findByAreaCode(String areaCode, Pageable pageable);

}
