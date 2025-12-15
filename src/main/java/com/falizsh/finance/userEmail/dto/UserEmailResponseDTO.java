package com.falizsh.finance.userEmail.dto;


import com.falizsh.finance.userEmail.model.EmailStatus;
import com.falizsh.finance.userEmail.model.UserEmailType;
import com.falizsh.finance.userEmail.model.UserEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailResponseDTO {

    private Long id;
    private UserEmailType userEmailType;
    private String email;
    private Boolean isPrimary;
    private EmailStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserEmailResponseDTO(UserEmail userEmail) {
        this.id = userEmail.getId();
        this.userEmailType = userEmail.getType();
        this.email = userEmail.getEmail();
        this.isPrimary = userEmail.getIsPrimary();
        this.status = userEmail.getStatus();
        this.createdAt = userEmail.getCreatedAt();
        this.updatedAt = userEmail.getUpdatedAt();
    }

}
