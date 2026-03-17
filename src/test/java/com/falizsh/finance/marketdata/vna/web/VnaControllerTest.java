package com.falizsh.finance.marketdata.vna.web;

import com.falizsh.finance.marketdata.vna.usecase.FetchVnaDataUseCase;
import com.falizsh.finance.marketdata.vna.usecase.ImportVnaDataRangeUseCase;
import com.falizsh.finance.marketdata.vna.usecase.ImportVnaDataUseCase;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class VnaControllerTest extends TestSupport {

    @Mock
    private ImportVnaDataUseCase importVnaDataUseCase;

    @Mock
    private ImportVnaDataRangeUseCase importVnaDataRangeUseCase;

    @Mock
    private FetchVnaDataUseCase fetchVnaDataUseCase;

    private VnaController vnaController;

    @Override
    public void init() {
        this.vnaController = new VnaController(importVnaDataUseCase, importVnaDataRangeUseCase, fetchVnaDataUseCase);
    }

    @Test
    void shouldImportVnaUsingTodayWhenDateIsNull() {
        vnaController.importVna(null);

        verify(importVnaDataUseCase).execute(any(LocalDate.class));
    }

    @Test
    void shouldImportVnaUsingProvidedDate() {
        LocalDate date = LocalDate.of(2023, 10, 10);

        vnaController.importVna(date);

        verify(importVnaDataUseCase).execute(eq(date));
    }

    @Test
    void shouldThrowExceptionWhenRangeEndDateIsBeforeStartDate() {
        LocalDate startDate = LocalDate.of(2023, 10, 10);
        LocalDate endDate = LocalDate.of(2023, 10, 9);

        assertThatThrownBy(() -> vnaController.importRange(startDate, endDate))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseEx = (ResponseStatusException) ex;
                    assertThat(responseEx.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(responseEx.getReason()).isEqualTo("A data final não pode ser anterior à data inicial.");
                });
    }

    @Test
    void shouldThrowExceptionWhenRangeDatesAreInFuture() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        assertThatThrownBy(() -> vnaController.importRange(startDate, endDate))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseEx = (ResponseStatusException) ex;
                    assertThat(responseEx.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(responseEx.getReason()).contains("As datas de importação não podem ser futuras");
                });
    }

    @Test
    void shouldCallFetchUseCaseUsingTodayWhenDateIsNull() {
        vnaController.fetchVna(null);

        verify(fetchVnaDataUseCase).execute(any(LocalDate.class));
    }
}