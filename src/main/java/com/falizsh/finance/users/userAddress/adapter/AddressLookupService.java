package com.falizsh.finance.users.userAddress.adapter;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.users.userAddress.adapter.dto.AddressInfoDTO;

public interface AddressLookupService {
    AddressInfoDTO findAddressByZipCode(CEP cep);
}
