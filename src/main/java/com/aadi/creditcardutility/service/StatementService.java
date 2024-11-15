package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;

import java.util.List;

public interface StatementService {

    List<TransactionDTO> convertStatementToTransactions(StatementFilePOJO statement);

    CreditType populateCreditType(String value);

    TransactionDTO prepareTransactionDTOFromStatementLine(CreditCardDTO cardDTO, String line);

    void applyMaxLimitOnReward(List<TransactionDTO> transactionDTOs);
}
