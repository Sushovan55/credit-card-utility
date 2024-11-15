package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.entity.Transaction;
import com.aadi.creditcardutility.mapper.TransactionMapper;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.pojo.TransactionPOJO;
import com.aadi.creditcardutility.repository.TransactionRepository;
import com.aadi.creditcardutility.service.CreditCardService;
import com.aadi.creditcardutility.service.StatementFactory;
import com.aadi.creditcardutility.service.StatementService;
import com.aadi.creditcardutility.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private StatementFactory statementFactory;
    private CreditCardService cardService;

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = TransactionMapper.toTransaction(transactionDTO);
        Transaction persistedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toTransactionDTO(persistedTransaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(TransactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> createTransactionByStatement(StatementFilePOJO statement) {

        StatementService statementService = statementFactory.getStatementService(statement);
        List<TransactionDTO> transactionDTOs =
                statementService.convertStatementToTransactions(statement);

        List<Transaction> transactions = transactionDTOs.stream()
                        .map(TransactionMapper::toTransaction)
                        .toList();

            /*transactionDTOs.forEach(System.out::println);
            System.out.println(transactionDTOs.stream().mapToDouble(TransactionDTO::getAcquiredReward).sum());*/

        List<Transaction> persistedTransactions = transactionRepository.saveAll(transactions);

        return persistedTransactions.stream().map(TransactionMapper::toTransactionDTO).toList();
    }

    @Override
    public List<TransactionDTO> getTransactionForMonthAndYear(TransactionPOJO transaction) {
        CreditCardDTO cardDTO = cardService.getCreditCardByDisplayName(transaction.getCreditCardName());

        LocalDate fromDate = LocalDate.of(
                transaction.getYear(),
                transaction.getMonth().getValue() - 1,
                cardDTO.getBillingStart());

        LocalDate toDate = LocalDate.of(
                transaction.getYear(),
                transaction.getMonth().getValue(),
                cardDTO.getBillingEnd());

        List<Transaction> transactions =
                transactionRepository.findByCardIdWithDuration(cardDTO.getId(), fromDate, toDate)
                        .orElse(List.of(new Transaction()));

        return transactions.stream().map(TransactionMapper::toTransactionDTO).toList();
    }

    public List<TransactionDTO> getTransactionByDuration(TransactionPOJO transaction) {
        CreditCardDTO cardDTO = cardService.getCreditCardByDisplayName(transaction.getCreditCardName());

        List<Transaction> transactions =
                transactionRepository.findByCardIdWithDuration(
                                cardDTO.getId(),
                                transaction.getFromDate(),
                                transaction.getToDate())
                        .orElse(List.of(new Transaction()));

        return transactions.stream().map(TransactionMapper::toTransactionDTO).toList();
    }


}
