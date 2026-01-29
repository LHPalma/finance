package com.falizsh.finance.shared.bcbifdata.adapter;

import com.falizsh.finance.config.feign.FeignNativeConfig;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

// TODO: Não está funcional. A API do Bacen está retornando 500 para todas as datas no endpoint IfDataCadastro.
//       Avaliar em outro momento

@FeignClient(
        name = "bcbIfDataClient",
        url = "${bcb.base-url-odata}",
        configuration = FeignNativeConfig.class
)
public interface BcbIfDataClient {

    @RequestLine("GET /IfDataCadastro?$filter={filter}&$format={format}")
    String fetchInstituition(
            @Param("filter") String filter,
            @Param("format") String format
    );

    @RequestLine("GET /ListaDeRelatorio?$filter={filter}&$format={format}")
    String fetchReports(
            @Param("filter") String filter,
            @Param("format") String format
    );

    @RequestLine("GET /IfDataValores?$filter={filter}&$format={format}")
    String fetchValues(
            @Param("filter") String filter,
            @Param("format") String format
    );

}
