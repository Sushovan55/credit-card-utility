package com.aadi.creditcardutility.controller;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/credit-card")
public class CreditCardController {

    private CreditCardService cardService;

    @GetMapping("/all")
    public ResponseEntity<List<CreditCardDTO>> getAllCreditCards() {
        List<CreditCardDTO> cards = cardService.getAllCreditCards();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/by-bank/{bankId}")
    public ResponseEntity<List<CreditCardDTO>> getAllCreditCardsByBank(
            @PathVariable Integer bankId) {
        List<CreditCardDTO> cards = cardService.getAllCreditCardsByBank(bankId);
        return ResponseEntity.ok(cards);
    }

}
