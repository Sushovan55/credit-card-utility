package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.pojo.TransactionPOJO;

import java.time.Month;
import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    List<TransactionDTO> getAllTransactions();

    List<TransactionDTO> createTransactionByStatement(StatementFilePOJO statement);

    List<TransactionDTO> getTransactionForMonthAndYear(TransactionPOJO transaction);

    List<TransactionDTO> getTransactionByDuration(TransactionPOJO transaction);

}
