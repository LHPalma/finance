package com.falizsh.finance.shared.bcbifdata.adapter;

import com.falizsh.finance.config.feign.BacenFeingConfig;
import com.falizsh.finance.config.feign.FeignNativeConfig;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "bcbIfDataClient",
        url = "${bcb.base-url-odata}",
        configuration = {FeignNativeConfig.class, BacenFeingConfig.class}
)
public interface BcbIfDataClient {

    @RequestLine(value = "GET /IfDataCadastro(AnoMes=@AnoMes)?@AnoMes={yearMonth}&$format=json&$filter={filter}", decodeSlash = false)
    String fetchInstituition(
            @Param("yearMonth") Integer yearMonth,
            @Param("filter") String filter
    );

    @RequestLine(value = "GET /ListaDeRelatorio()?$filter={filter}&$format=json", decodeSlash = false)
    String fetchReports(@Param("filter") String filter);

    @RequestLine(value = "GET /IfDataValores(AnoMes=@AnoMes,TipoInstituicao=@TipoInstituicao,Relatorio=@Relatorio)?@AnoMes={yearMonth}&@TipoInstituicao={tipoInst}&@Relatorio={idReport}&$filter={filter}&$format=json", decodeSlash = false)
    String fetchValues(
            @Param("yearMonth") Integer yearMonth,
            @Param("tipoInst") Integer tipoInst,
            @Param("idReport") String idReport,
            @Param("filter") String filter
    );

}
