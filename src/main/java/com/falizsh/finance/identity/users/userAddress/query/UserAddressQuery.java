package com.falizsh.finance.identity.users.userAddress.query;

import com.falizsh.finance.identity.users.userAddress.model.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAddressQuery {
    Page<UserAddress> findAll(Pageable pageable);
}
