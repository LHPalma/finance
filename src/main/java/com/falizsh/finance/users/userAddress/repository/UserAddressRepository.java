package com.falizsh.finance.users.userAddress.repository;

import com.falizsh.finance.users.userAddress.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
