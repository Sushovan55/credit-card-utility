package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.entity.CreditCard;
import com.aadi.creditcardutility.mapper.CreditCardMapper;
import com.aadi.creditcardutility.repository.CreditCardRepository;
import com.aadi.creditcardutility.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CreditCardServiceImpl implements CreditCardService {

    private CreditCardRepository creditCardRepository;
    @Override
    public List<CreditCardDTO> getAllCreditCards() {
        List<CreditCard> cards = creditCardRepository.findAll();
        return cards.stream()
                .map(CreditCardMapper::toCreditCardDTO)
                .toList();
    }

    @Override
    public List<CreditCardDTO> getAllCreditCardsByBank(Integer bankId) {
        List<CreditCard> cards = creditCardRepository.findByBankId(bankId);
        return cards.stream()
                .map(CreditCardMapper::toCreditCardDTO)
                .collect(Collectors.toList());
    }
}