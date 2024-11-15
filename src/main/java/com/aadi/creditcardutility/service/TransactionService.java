package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    List<TransactionDTO> getAllTransactions();
    List<TransactionDTO> createTransactionByStatement(StatementFilePOJO statement);

}
