package com.aadi.creditcardutility.controller;

import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.pojo.TransactionPOJO;
import com.aadi.creditcardutility.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping("/statement")
    public ResponseEntity<String> createTransactionByStatement(
            @RequestBody StatementFilePOJO statement) {

        List<TransactionDTO> transactionDTOs =
                transactionService.createTransactionByStatement(statement);

        return ResponseEntity.ok(transactionDTOs.size() + " transaction/s inserted");
    }

    @GetMapping("/month")
    public ResponseEntity<List<TransactionDTO>> getTransactionByMonth(
            @RequestBody TransactionPOJO transactionPOJO) {

        List<TransactionDTO> transactionDTOs =
                transactionService.getTransactionForMonthAndYear(transactionPOJO);

        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping("/duration")
    public ResponseEntity<List<TransactionDTO>> getTransactionByDuration(
            @RequestBody TransactionPOJO transactionPOJO) {

        List<TransactionDTO> transactionDTOs =
                transactionService.getTransactionByDuration(transactionPOJO);

        return ResponseEntity.ok(transactionDTOs);
    }
}
