package com.falizsh.finance.portfolio.bankAccount.domain.repository.ledger;

import com.falizsh.finance.portfolio.bankAccount.domain.model.ledger.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
}
