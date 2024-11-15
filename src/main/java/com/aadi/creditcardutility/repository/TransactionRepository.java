package com.aadi.creditcardutility.repository;

import com.aadi.creditcardutility.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.card.id = ?1 AND t.transactOn BETWEEN ?2 AND ?3")
    Optional<List<Transaction>> findByCardIdWithDuration(Integer cardId, LocalDate from, LocalDate to);
}
