package com.aadi.creditcardutility.repository;

import com.aadi.creditcardutility.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
