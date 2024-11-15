package com.aadi.creditcardutility.mapper;

import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.entity.Transaction;

public class TransactionMapper {

    public static TransactionDTO toTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                CreditCardMapper.toCreditCardDTO(transaction.getCard()),
                transaction.getTransactionDetail(),
                transaction.getTransactionType(),
                transaction.getTransactionNo(),
                transaction.getTransactionAmount(),
                transaction.getTransactOn(),
                transaction.getCreditType(),
                transaction.getAcquiredReward()
        );
    }

    public static Transaction toTransaction(TransactionDTO transactionDTO) {
        return new Transaction(
                transactionDTO.getId(),
                CreditCardMapper.toCreditCard(transactionDTO.getCard()),
                transactionDTO.getTransactionDetail(),
                transactionDTO.getTransactionType(),
                transactionDTO.getTransactionNo(),
                transactionDTO.getTransactionAmount(),
                transactionDTO.getTransactOn(),
                transactionDTO.getCreditType(),
                transactionDTO.getAcquiredReward()
        );
    }
}
