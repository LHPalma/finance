package com.falizsh.finance.userAddress.web;


import com.falizsh.finance.userAddress.assembler.UserAddressAssemblerSuport;
import com.falizsh.finance.userAddress.model.UserAddress;
import com.falizsh.finance.userAddress.query.UserAddressQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user/address")
public class UserAddressController {

    private final UserAddressQuery query;
    private final UserAddressAssemblerSuport assembler;

    @GetMapping
    public PagedModel<EntityModel<UserAddress>> findAll(
            Pageable pageable,
            PagedResourcesAssembler<UserAddress> pagedAssembler
    ) {

        return pagedAssembler.toModel(query.findAll(pageable), assembler);

    }


}
