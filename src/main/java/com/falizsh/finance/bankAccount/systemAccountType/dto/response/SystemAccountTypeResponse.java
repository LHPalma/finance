package com.falizsh.finance.bankAccount.systemAccountType.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SystemAccountTypeResponse {

    private Long id;
    private String name;
    private String description;
    private boolean allowsOverdraft;
    private boolean isActive;

}