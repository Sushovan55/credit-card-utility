package com.aadi.creditcardutility.controller;

import com.aadi.creditcardutility.dto.BankDTO;
import com.aadi.creditcardutility.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/bank")
public class BankController {

    private BankService bankService;

    @PostMapping
    public ResponseEntity<BankDTO> createBank(@RequestBody BankDTO bankDTO) {
        BankDTO savedBank = bankService.createBank(bankDTO);
        return new ResponseEntity<>(savedBank, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<BankDTO> getBankById(@PathVariable Integer id) {
        BankDTO bankDto = bankService.getBankById(id);
        return ResponseEntity.ok(bankDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BankDTO>> getAllBanks() {
        List<BankDTO> banks = bankService.getAllBanks();
        return ResponseEntity.ok(banks);
    }

    @PutMapping("{id}")
    public ResponseEntity<BankDTO> updateBank(@PathVariable Integer id, @RequestBody BankDTO bankDto) {
        BankDTO updatedBank = bankService.updateBank(id, bankDto);
        return ResponseEntity.ok(updatedBank);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBank(@PathVariable Integer id) {
        bankService.deleteBank(id);
        return ResponseEntity.ok("Bank deleted successfully");
    }
}
