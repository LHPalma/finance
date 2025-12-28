package com.falizsh.finance.users.userAddress.query;

import com.falizsh.finance.users.userAddress.model.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAddressQuery {
    Page<UserAddress> findAll(Pageable pageable);
}
