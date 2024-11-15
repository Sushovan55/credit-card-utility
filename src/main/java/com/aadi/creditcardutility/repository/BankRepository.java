package com.aadi.creditcardutility.repository;

import com.aadi.creditcardutility.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BankRepository extends JpaRepository<Bank, Integer> {
    Optional<List<Bank>> findByDisplayName(String displayName);
}
