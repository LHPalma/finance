package com.falizsh.finance.users.user.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        String status
) {

    public static UserResponse of(Long id, String name, String email, String status) {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .status(status)
                .build();
    }

}