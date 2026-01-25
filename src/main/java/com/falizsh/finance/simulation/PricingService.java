package com.falizsh.finance.simulation;

import com.falizsh.finance.shared.holiday.model.CountryCode;
import com.falizsh.finance.shared.holiday.model.Holiday;
import com.falizsh.finance.shared.holiday.repository.HolidayRepository;
import com.falizsh.finance.shared.utils.FinancialCalculous;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final HolidayRepository holidayRepository;

    /**
     * Calcula o preço da NTN-B (IPCA+) considerando a contagem exata de dias úteis.
     *
     * @param vna         Valor Nominal Atualizado
     * @param annualYield Taxa de rendimento (ex: 0.06 para 6%)
     * @param dataCalculo Data de referência (geralmente liquidação)
     * @param dueDate     Data de vencimento do título
     * @return Preço Unitário (PU)
     */
    public BigDecimal calculateNtnbPrice(BigDecimal vna, BigDecimal annualYield, BigDecimal projectedInflation, LocalDate dataCalculo, LocalDate dueDate) {

        LocalDate settlementDate = getSettlementDate(dataCalculo, 2);

        LocalDate firstDayOfMonth = settlementDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = settlementDate.with(TemporalAdjusters.lastDayOfMonth());
        int totalMonthBusinessDays = countBusinessDays(firstDayOfMonth.minusDays(1), lastDayOfMonth);

        int overdueBusinessDays = countBusinessDays(dataCalculo, settlementDate);

        BigDecimal inflation = projectedInflation != null ? projectedInflation : BigDecimal.ZERO;

        BigDecimal projectedVna = FinancialCalculous.calculateProjectedVna(
                vna,
                inflation,
                overdueBusinessDays,
                totalMonthBusinessDays
        );

        int businessDaysToMaturity = countBusinessDays(settlementDate.minusDays(1), dueDate);


        return FinancialCalculous.calculateNtnbUnitPrice(projectedVna, annualYield, businessDaysToMaturity);
    }

    private boolean isBusinessDay(LocalDate date, Set<LocalDate> holidaysSet) {
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) return false;
        return !holidaysSet.contains(date);
    }

    /**
     * Conta dias úteis entre duas datas (Data Inicial (start) excluída, Data Final (end) incluída).
     * Padrão de mercado brasileiro (overnight).
     */
    public int countBusinessDays(LocalDate start, LocalDate end) {
        if (start.isAfter(end) || start.equals(end)) return 0;

        // Busca todos os feriados no intervalo de uma vez
        Set<LocalDate> holidaysSet = holidayRepository
                .findByCountryCodeAndDateBetween(CountryCode.BR, start, end)
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        int businessDays = 0;
        LocalDate current = start.plusDays(1);

        while (!current.isAfter(end)) {
            if (isBusinessDay(current, holidaysSet)) {
                businessDays++;
            }
            current = current.plusDays(1);
        }
        return businessDays;
    }

    private boolean isWeekDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    public LocalDate getSettlementDate(LocalDate calculationDate, int businessDaysToSettle) {
        LocalDate tradeDate = calculationDate;
        Set<LocalDate> holidays = getHolidaysInWindow(tradeDate, 15);

        int added = 0;
        while (added < businessDaysToSettle) {
            tradeDate = tradeDate.plusDays(1);
            if (isBusinessDay(tradeDate, holidays)) {
                added++;
            }
        }
        return tradeDate;
    }

    /**
     * Calcula a data de liquidação somando N dias úteis.
     */
    private Set<LocalDate> getHolidaysInWindow(LocalDate startDate, int daysWindow) {
        return holidayRepository
                .findByCountryCodeAndDateBetween(CountryCode.BR, startDate, startDate.plusDays(daysWindow))
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());
    }

}
