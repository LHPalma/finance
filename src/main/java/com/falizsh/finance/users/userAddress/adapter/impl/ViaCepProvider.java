package com.falizsh.finance.users.userAddress.adapter.impl;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.users.userAddress.adapter.AddressLookupProvider;
import com.falizsh.finance.users.userAddress.adapter.ViaCepClient;
import com.falizsh.finance.users.userAddress.adapter.dto.AddressInfoDTO;
import com.falizsh.finance.users.userAddress.adapter.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ViaCepProvider implements AddressLookupProvider {

    private final ViaCepClient client;

    @Override
    public AddressInfoDTO findAddress(CEP cep) {
        try {

            ViaCepResponse response = client.findCep(cep.getUnformatted());

            if (response == null || Boolean.TRUE.equals(response.erro())) {
                log.warn("CEP não encontrado");
                return null;
            }
            return AddressInfoDTO.builder()
                    .street(response.logradouro())
                    .neighborhood(response.bairro())
                    .city(response.localidade())
                    .state(response.uf())
                    .complement(response.complemento())
                    .build();

        } catch (Exception e) {
            log.error("Falha ao comunicar com ViaCEP", e);
            return null;
        }
    }

    @Override
    public int getPriorityOrder() {
        return 1;
    }

    @Override
    public String getProviderName() {
        return "ViaCEP";
    }
}
