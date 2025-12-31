package com.falizsh.finance.users.userAddress.adapter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressInfoDTO {

    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String complement;

}
