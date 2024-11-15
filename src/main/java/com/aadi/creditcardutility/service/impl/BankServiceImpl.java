package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.BankDTO;
import com.aadi.creditcardutility.entity.Bank;
import com.aadi.creditcardutility.exception.ResourceNotFoundException;
import com.aadi.creditcardutility.mapper.BankMapper;
import com.aadi.creditcardutility.repository.BankRepository;
import com.aadi.creditcardutility.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BankServiceImpl implements BankService {

    private BankRepository bankRepository;


    @Override
    public BankDTO createBank(BankDTO bankDTO) {
        Bank bank = BankMapper.toBank(bankDTO);
        Bank persistedBank = bankRepository.save(bank);
        return BankMapper.toBankDTO(persistedBank);
    }

    public BankDTO getBankById(Integer id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bank not found with id: " + id));
        return BankMapper.toBankDTO(bank);
    }

    @Override
    public List<BankDTO> getAllBanks() {
        List<Bank> banks = bankRepository.findAll();
        return banks.stream()
                .map(BankMapper::toBankDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankDTO updateBank(Integer id, BankDTO updatedBankDTO) {
        Bank bank = bankRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Bank not found with id: " + id));

        bank.setName(updatedBankDTO.getName());
        bank.setDisplayName(updatedBankDTO.getDisplayName());
        Bank updatedBank = bankRepository.save(bank);

        return BankMapper.toBankDTO(updatedBank);
    }

    @Override
    public void deleteBank(Integer id) {
        Bank bank = bankRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Bank not found with id: " + id));

        bankRepository.deleteById(id);
    }


}
