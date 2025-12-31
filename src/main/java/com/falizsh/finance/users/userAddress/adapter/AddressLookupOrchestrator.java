package com.falizsh.finance.users.userAddress.adapter;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.users.userAddress.adapter.dto.AddressInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Primary
@Service
public class AddressLookupOrchestrator implements AddressLookupService {

    private final List<AddressLookupProvider> providers;

    @Override
    public AddressInfoDTO findAddressByZipCode(CEP cep) {
        return providers.stream()
                .sorted(Comparator.comparingInt(AddressLookupProvider::getPriorityOrder))
                .map(provider -> provider.findAddress(cep))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
