package com.aadi.creditcardutility.mapper;

import com.aadi.creditcardutility.dto.BankDTO;
import com.aadi.creditcardutility.entity.Bank;

public class BankMapper {
    public static BankDTO toBankDTO(Bank bank) {
        return new BankDTO(
                bank.getId(),
                bank.getName(),
                bank.getDisplayName());
    }

    public static Bank toBank(BankDTO bankDTO) {
        return new Bank(
                bankDTO.getId(),
                bankDTO.getName(),
                bankDTO.getDisplayName());
    }
}
