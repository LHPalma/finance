package com.falizsh.finance.identity.users.userAddress.repository;

import com.falizsh.finance.identity.users.userAddress.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
