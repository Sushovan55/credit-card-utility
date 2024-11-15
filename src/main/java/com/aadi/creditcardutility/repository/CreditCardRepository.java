package com.aadi.creditcardutility.repository;

import com.aadi.creditcardutility.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    Optional<List<CreditCard>> findByBankId(Integer bankId);

    Optional<List<CreditCard>> findByDisplayName(String displayName);
}
