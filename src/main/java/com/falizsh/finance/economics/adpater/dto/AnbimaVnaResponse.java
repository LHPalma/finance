package com.falizsh.finance.economics.adpater.dto;

import com.falizsh.finance.economics.model.Vna;
import com.falizsh.finance.economics.model.VnaStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hibernate.boot.jaxb.mapping.DiscriminatedAssociation;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "VNA")
public record AnbimaVnaResponse(

        @JacksonXmlProperty(localName = "Titulo")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<AnbimaSecurity> securityList

) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnbimaSecurity(
            @JacksonXmlProperty(isAttribute = true, localName = "Papel")
            String security,

            @JacksonXmlProperty(localName = "Dados")
            AnbimaSecurityData data
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnbimaSecurityData(
            @JacksonXmlProperty(isAttribute = true, localName = "Cod_Selic")
            String selicCode,

            @JacksonXmlProperty(isAttribute = true, localName = "DtRef_D0")
            String referenceDate,

            @JacksonXmlProperty(isAttribute = true, localName = "Valor_D0")
            String price,

            @JacksonXmlProperty(isAttribute = true, localName = "Index_D0")
            String indexValue,

            @JacksonXmlProperty(isAttribute = true, localName = "Ref_D0")
            String status
    ) {

    }

}
