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
public class FlipkartAxisStatementServiceImpl implements StatementService {

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

                Pattern pattern = Pattern.compile(Constants.FLIPKART_AXIS_STATEMENT_REGEX);
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
        for (String billPayment : Constants.TRANSACTION_AXIS_BILL_PAYMENTS) {
            if (value.startsWith(billPayment)) {
                return CreditType.BillPayment;
            }
        }
        if (value.startsWith(Constants.TRANSACTION_AXIS_CASHBACK)) {
            creditType = CreditType.Cashback;
        } else {
            creditType = CreditType.Refund;
        }
        return creditType;
    }

    @Override
    public TransactionDTO prepareTransactionDTOFromStatementLine(Matcher matcher, CreditCardDTO cardDTO) {
        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setCard(cardDTO);
        transactionDTO.setTransactionDetail(matcher.group(2));

        if (Constants.AXIS_CREDIT.equals(matcher.group(4))) {
            transactionDTO.setTransactionType(TransactionType.Credit);
            transactionDTO.setCreditType(populateCreditType(transactionDTO.getTransactionDetail()));
        } else {
            transactionDTO.setTransactionType(TransactionType.Debit);
        }

        transactionDTO.setTransactionAmount(
                Double.parseDouble(matcher.group(3).replaceAll(Constants.COMMA, Constants.EMPTY_STRING)));
        transactionDTO.setTransactOn(LocalDate.parse(matcher.group(1),
                DateTimeFormatter.ofPattern(Constants.STATEMENT_DATE_FORMAT)));

        if (Constants.AXIS_DEBIT.equals(matcher.group(6))
                && Double.parseDouble(matcher.group(5)) > 0) {
            transactionDTO.setAcquiredReward(Double.parseDouble(Constants.HYPHEN + matcher.group(5)));
        } else {
            transactionDTO.setAcquiredReward(Double.parseDouble(matcher.group(5)));
        }

        return transactionDTO;
    }
}
