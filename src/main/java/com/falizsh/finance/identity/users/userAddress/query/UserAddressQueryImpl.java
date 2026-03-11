package com.falizsh.finance.identity.users.userAddress.query;

import com.falizsh.finance.identity.users.userAddress.model.UserAddress;
import com.falizsh.finance.identity.users.userAddress.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAddressQueryImpl implements UserAddressQuery{

    private final UserAddressRepository repository;

    @Override
    public Page<UserAddress> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
