package com.falizsh.finance.bankAccount.systemAccountType.web.assembler;

import com.falizsh.finance.bankAccount.systemAccountType.dto.response.SystemAccountTypeResponse;
import com.falizsh.finance.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.bankAccount.systemAccountType.web.SystemAccountTypeController;
import com.falizsh.finance.users.user.web.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SystemAccountTypeAssembler implements RepresentationModelAssembler<SystemAccountType, EntityModel<SystemAccountTypeResponse>> {
    @Override
    public EntityModel<SystemAccountTypeResponse> toModel(SystemAccountType entity) {

        if (entity == null) {
            return null;
        }

        SystemAccountTypeResponse response = SystemAccountTypeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .allowsOverdraft(entity.isAllowsOverdraft())
                .isActive(entity.isActive())
                .build();


        return EntityModel.of(
                response,
                linkTo(methodOn(SystemAccountTypeController.class).getById(entity.getId())).withSelfRel()
        );

    }
}
