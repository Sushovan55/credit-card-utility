package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.entity.CreditCard;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.mapper.CreditCardMapper;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.repository.CreditCardRepository;
import com.aadi.creditcardutility.service.StatementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class StatementServiceImpl implements StatementService {

    protected CreditCardRepository cardRepository;

    @Override
    public List<TransactionDTO> convertStatementToTransactions(StatementFilePOJO statement) {
        //Read from file path
        File file = new File(statement.getFilePath());
        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        //Get the card to attach with transactions
        List<CreditCard> cards =
                cardRepository.findByDisplayName(statement.getCreditCardName())
                        .orElseThrow(() ->
                                new EntityNotFoundException("CreditCard not found with display name: "
                                        + statement.getCreditCardName()));
        CreditCardDTO cardDTO = CreditCardMapper.toCreditCardDTO(cards.get(0));

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                transactionDTOs.add(prepareTransactionDTOFromStatementLine(cardDTO, line));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return transactionDTOs;
    }

    @Override
    public CreditType populateCreditType(String value) {
        return null;
    }

    @Override
    public TransactionDTO prepareTransactionDTOFromStatementLine(CreditCardDTO cardDTO, String line) {
        return null;
    }

    @Override
    public void applyMaxLimitOnReward(List<TransactionDTO> transactionDTOs) {
    }
}
