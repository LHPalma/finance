package com.falizsh.finance.shared.web;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.users.userAddress.adapter.AddressLookupService;
import com.falizsh.finance.users.userAddress.adapter.dto.AddressInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/finance/utils/cep")
@RequiredArgsConstructor
public class CepLookupController {

    private final AddressLookupService lookupService;

    @GetMapping("/lookup/{zipCode}")
    public ResponseEntity<AddressInfoDTO> getAddressZipCode(@PathVariable String zipCode) {
        CEP cep = CEP.unverified(zipCode);

        AddressInfoDTO foundAddress = lookupService.findAddressByZipCode(cep);

        if (foundAddress == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundAddress);
    }

}
