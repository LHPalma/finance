package com.falizsh.finance.userTelephone.repository;

import com.falizsh.finance.userTelephone.model.UserTelephone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserTelephoneQuery {

    Page<UserTelephone> findAll(Pageable pageable);

    Page<UserTelephone> findByUserId(Long userId, Pageable pageable);

    Page<UserTelephone> findByAreaCode(String areaCode, Pageable pageable);

}
