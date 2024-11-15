package com.aadi.creditcardutility.mapper;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.entity.CreditCard;

public class CreditCardMapper {

    public static CreditCardDTO toCreditCardDTO(CreditCard card) {
        return new CreditCardDTO(
                card.getId(),
                card.getName(),
                card.getDisplayName(),
                BankMapper.toBankDTO(card.getBank()),
                card.getCardDetailId(),
                card.getMonthlyLimit(),
                card.getJoiningFee(),
                card.getAnnualFee(),
                card.getLoungeAccess(),
                card.getActivatedOn(),
                card.getBillingStart(),
                card.getBillingEnd(),
                card.getStatus());
    }

    public static CreditCard toCreditCard(CreditCardDTO cardDTO) {
        return new CreditCard(
                cardDTO.getId(),
                cardDTO.getName(),
                cardDTO.getDisplayName(),
                BankMapper.toBank(cardDTO.getBank()),
                cardDTO.getCardDetailId(),
                cardDTO.getMonthlyLimit(),
                cardDTO.getJoiningFee(),
                cardDTO.getAnnualFee(),
                cardDTO.getLoungeAccess(),
                cardDTO.getActivatedOn(),
                cardDTO.getBillingStart(),
                cardDTO.getBillingEnd(),
                cardDTO.getStatus());
    }
}
