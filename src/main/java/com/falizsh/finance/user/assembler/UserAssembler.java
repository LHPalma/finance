package com.falizsh.finance.user.assembler;

import com.falizsh.finance.email.web.UserEmailController;
import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.web.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(UserController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserEmailController.class).findAllByUserId(entity.getId(), null, null)).withRel("emails"),
                linkTo(methodOn(UserController.class).findAll(null, null)).withRel("users")
        );
    }
}
