package com.falizsh.finance.email.assembler;

import com.falizsh.finance.email.dto.UserEmailCreateDTO;
import com.falizsh.finance.email.dto.UserEmailResponseDTO;
import com.falizsh.finance.email.model.EmailStatus;
import com.falizsh.finance.email.model.UserEmail;
import com.falizsh.finance.user.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserEmailAssembler implements RepresentationModelAssembler<UserEmail, EntityModel<UserEmailResponseDTO>> {

    @Override
    public EntityModel<UserEmailResponseDTO> toModel(UserEmail entity) {
        UserEmailResponseDTO dto = new UserEmailResponseDTO(entity);
        return EntityModel.of(dto);
    }

    public UserEmail toEntity(UserEmailCreateDTO dto, User user) {
        UserEmail entity = new UserEmail();
        entity.setUser(user);
        entity.setType(dto.getType());
        entity.setEmail(dto.getEmail());
        entity.setIsPrimary(dto.getIsPrimary());
        entity.setStatus(EmailStatus.ACTIVE);

        return entity;
    }

}
