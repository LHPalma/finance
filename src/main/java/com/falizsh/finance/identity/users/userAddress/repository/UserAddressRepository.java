package com.falizsh.finance.identity.users.userAddress.repository;

import com.falizsh.finance.identity.users.userAddress.model.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    Page<UserAddress> findAllByUserId(Long userId, Pageable pageable);

    Page<UserAddress> findAllByUserIdAndIsPrimary(Long userId, Boolean isPrimary, Pageable pageable);

}
