package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.entity.CreditCard;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.TransactionType;
import com.aadi.creditcardutility.mapper.CreditCardMapper;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.repository.CreditCardRepository;
import com.aadi.creditcardutility.service.StatementService;
import com.aadi.creditcardutility.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class AmazonPayICICIStatementServiceImpl implements StatementService {

    private CreditCardRepository cardRepository;

    @Override
    public List<TransactionDTO> convertStatementToTransactions(StatementFilePOJO statement) {

        //Read from file path
        File file = new File(statement.getFilePath());
        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        //Get the card to attach with transactions
        List<CreditCard> cards =
                cardRepository.findByDisplayName(statement.getCreditCardName())
                        .orElseThrow(EntityNotFoundException::new);
        CreditCardDTO cardDTO = CreditCardMapper.toCreditCardDTO(cards.get(0));

        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {

                Pattern pattern = Pattern.compile(Constants.AMAZON_PAY_ICICI_STATEMENT_REGEX);
                Matcher matcher = pattern.matcher(st);

                if (matcher.find()) {
                    transactionDTOs.add(prepareTransactionDTOFromStatementLine(matcher, cardDTO));
                }
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
        CreditType creditType;
        if (Constants.TRANSACTION_ICICI_BILL_PAYMENTS.contains(value)) {
            creditType = CreditType.BillPayment;
        } else if (Constants.TRANSACTION_ICICI_REVERSALS.contains(value)) {
            creditType = CreditType.Reversal;
        } else {
            creditType = CreditType.Refund;
        }
        return creditType;
    }

    @Override
    public TransactionDTO prepareTransactionDTOFromStatementLine(Matcher matcher, CreditCardDTO cardDTO) {
        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setCard(cardDTO);
        transactionDTO.setTransactionDetail(matcher.group(3));

        if (StringUtils.isAllEmpty(matcher.group(6))) {
            transactionDTO.setTransactionType(TransactionType.Debit);
        } else {
            transactionDTO.setTransactionType(TransactionType.Credit);
            transactionDTO.setCreditType(populateCreditType(transactionDTO.getTransactionDetail()));
        }

        transactionDTO.setTransactionNo(matcher.group(2));
        transactionDTO.setTransactionAmount(
                Double.parseDouble(matcher.group(5).replaceAll(Constants.COMMA, Constants.EMPTY_STRING)));
        transactionDTO.setTransactOn(LocalDate.parse(matcher.group(1),
                DateTimeFormatter.ofPattern(Constants.STATEMENT_DATE_FORMAT)));

        transactionDTO.setAcquiredReward(Double.parseDouble(matcher.group(4)));

        return transactionDTO;
    }
}
