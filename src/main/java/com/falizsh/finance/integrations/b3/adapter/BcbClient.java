package com.falizsh.finance.integrations.b3.adapter;


import com.falizsh.finance.integrations.b3.adapter.dto.BcbSgsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "bcbClient",url = "https://api.bcb.gov.br")
public interface BcbClient {

    @GetMapping("/dados/serie/bcdata.sgs.{serie}/dados/ultimos/{quantity}?formato=json")
    List<BcbSgsResponse> getLastValue(
            @PathVariable Long serie,
            @PathVariable Integer quantity
    );

}
