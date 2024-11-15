package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.dto.BankDTO;

import java.util.List;

public interface BankService {
    BankDTO createBank(BankDTO bankDto);
    BankDTO getBankById(Integer id);
    List<BankDTO> getAllBanks();
    BankDTO updateBank(Integer id, BankDTO updatedBankDTO);
    void deleteBank(Integer id);
}
