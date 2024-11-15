package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.dto.CreditCardDTO;

import java.util.List;

public interface CreditCardService {

    List<CreditCardDTO> getAllCreditCards();

    List<CreditCardDTO> getAllCreditCardsByBank(Integer bankId);
}
