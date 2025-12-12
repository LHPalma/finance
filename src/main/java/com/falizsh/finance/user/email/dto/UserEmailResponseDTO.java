package com.falizsh.finance.user.email.dto;


import com.falizsh.finance.user.email.model.EmailStatus;
import com.falizsh.finance.user.email.model.Type;
import com.falizsh.finance.user.email.model.UserEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailResponseDTO {

    private Long id;
    private Type type;
    private String email;
    private Boolean isPrimary;
    private EmailStatus emailStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserEmailResponseDTO(UserEmail userEmail) {
        this.id = userEmail.getId();
        this.type = userEmail.getType();
        this.email = userEmail.getEmail();
        this.isPrimary = userEmail.getIsPrimary();
        this.emailStatus = userEmail.getEmailStatus();
        this.createdAt = userEmail.getCreatedAt();
        this.updatedAt = userEmail.getUpdatedAt();
    }

}
