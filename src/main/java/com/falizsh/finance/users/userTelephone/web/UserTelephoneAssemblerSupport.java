package com.falizsh.finance.users.userTelephone.web;

import com.falizsh.finance.users.userTelephone.model.UserTelephone;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserTelephoneAssemblerSupport implements
        RepresentationModelAssembler<UserTelephone, EntityModel<UserTelephone>> {

    @Override
    public EntityModel<UserTelephone> toModel(UserTelephone entity) {
        return EntityModel.of(entity);
    }

}
