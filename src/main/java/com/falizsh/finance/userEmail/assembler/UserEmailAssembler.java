package com.falizsh.finance.userEmail.assembler;

import com.falizsh.finance.userEmail.dto.UserEmailCreateDTO;
import com.falizsh.finance.userEmail.dto.UserEmailResponseDTO;
import com.falizsh.finance.userEmail.model.UserEmail;
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


}
