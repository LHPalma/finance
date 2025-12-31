package com.falizsh.finance.users.userAddress.adapter;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.users.userAddress.adapter.dto.AddressInfoDTO;

public interface AddressLookupProvider {

    AddressInfoDTO findAddress(CEP cep);
    int getPriorityOrder();
    String getProviderName();

}
