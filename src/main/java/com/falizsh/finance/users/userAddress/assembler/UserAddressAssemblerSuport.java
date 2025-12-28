package com.falizsh.finance.users.userAddress.assembler;

import com.falizsh.finance.users.userAddress.model.UserAddress;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserAddressAssemblerSuport implements RepresentationModelAssembler<UserAddress, EntityModel<UserAddress>> {

    @Override
    public EntityModel<UserAddress> toModel(UserAddress entity) {
        return EntityModel.of(entity);
    }
}
