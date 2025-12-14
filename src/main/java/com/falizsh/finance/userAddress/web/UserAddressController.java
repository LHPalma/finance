package com.falizsh.finance.userAddress.web;


import com.falizsh.finance.userAddress.model.UserAddress;
import com.falizsh.finance.userAddress.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user/address")
public class UserAddressController {

    private final UserAddressRepository repository;

    @GetMapping
    public Page<UserAddress> findAll(Pageable pageable) {

        return repository.findAll(pageable);

    }

}
