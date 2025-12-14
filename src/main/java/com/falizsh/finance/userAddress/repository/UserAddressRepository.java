package com.falizsh.finance.userAddress.repository;

import com.falizsh.finance.userAddress.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
