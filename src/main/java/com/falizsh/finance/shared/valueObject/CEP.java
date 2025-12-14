package com.falizsh.finance.shared.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class CEP implements Serializable {

    @Column(name = "zip_code", length = 9, nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "zip_code_verification_status", nullable = false)
    private ZipCodeVerificationStatus verificationStatus = ZipCodeVerificationStatus.NOT_VERIFIED;

    private CEP(String cep, ZipCodeVerificationStatus status) {
        this.value = formatIfValid(cep);
        this.verificationStatus = status;
    }

    public static CEP unverified(String cep) {
        return new CEP(cep, ZipCodeVerificationStatus.NOT_VERIFIED);
    }

    public static CEP verified(String cep) {
        if (!isValidFormat(cep)) {
            return new CEP(cep, ZipCodeVerificationStatus.INVALID_FORMAT);
        }
        return new CEP(cep, ZipCodeVerificationStatus.VERIFIED);
    }

    public static CEP verificationFailed(String cep, ZipCodeVerificationStatus status) {
        return new CEP(cep, status);
    }

    private static boolean isValidFormat(String cep) {
        if (cep == null || cep.isBlank()) {
            return false;
        }
        String cleanCep = cep.replaceAll("[^0-9]", "");
        return cleanCep.length() == 8;
    }


    private String formatIfValid(String cep) {
        if (cep == null || cep.isBlank()) {
            return "";
        }

        String cleanCep = cep.replaceAll("[^0-9]", "");

        if (cleanCep.length() == 8) {
            return cleanCep.substring(0, 5) + "-" + cleanCep.substring(5);
        }

        return cep;
    }

    public String getUnformatted() {
        if (value == null)
            return "";
        return value.replaceAll("-", "");
    }

    public boolean isVerified() { // ✅ Método útil adicionado
        return verificationStatus == ZipCodeVerificationStatus.VERIFIED;
    }

    public boolean needsVerification() { // ✅ Método útil adicionado
        return verificationStatus == ZipCodeVerificationStatus.NOT_VERIFIED;
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }

}
