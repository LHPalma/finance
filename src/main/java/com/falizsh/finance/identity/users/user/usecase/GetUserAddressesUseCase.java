package com.falizsh.finance.identity.users.user.usecase;

import com.falizsh.finance.identity.users.userAddress.model.UserAddress;
import com.falizsh.finance.identity.users.userAddress.query.UserAddressQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserAddressesUseCase {

    private final UserAddressQuery query;

    public Page<UserAddress> execute(Long userId, Boolean isPrimary, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return query.findAllByUserIdAndIsPrimaryOptional(userId, isPrimary, pageRequest);

    }

}
