package com.falizsh.finance.identity.users.userAddress.adapter;

import com.falizsh.finance.infrastructure.valueObject.CEP;
import com.falizsh.finance.identity.users.userAddress.adapter.dto.AddressInfoDTO;

public interface AddressLookupService {
    AddressInfoDTO findAddressByZipCode(CEP cep);
}
