package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.TransactionType;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.repository.CreditCardRepository;
import com.aadi.creditcardutility.util.Constant;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FlipkartAxisStatementServiceImpl extends StatementServiceImpl {

    public FlipkartAxisStatementServiceImpl(CreditCardRepository cardRepository) {
        super(cardRepository);
    }

    @Override
    public List<TransactionDTO> convertStatementToTransactions(StatementFilePOJO statement) {
        return super.convertStatementToTransactions(statement);
    }

    @Override
    public CreditType populateCreditType(String value) {
        CreditType creditType;
        for (String billPayment : Constant.TRANSACTION_AXIS_BILL_PAYMENTS) {
            if (value.startsWith(billPayment)) {
                return CreditType.BillPayment;
            }
        }
        if (value.startsWith(Constant.TRANSACTION_AXIS_CASHBACK)) {
            creditType = CreditType.Cashback;
        } else {
            creditType = CreditType.Refund;
        }
        return creditType;
    }

    @Override
    public TransactionDTO prepareTransactionDTOFromStatementLine(CreditCardDTO cardDTO, String line) {

        Pattern pattern = Pattern.compile(Constant.FLIPKART_AXIS_STATEMENT_REGEX);
        Matcher matcher = pattern.matcher(line);
        TransactionDTO transactionDTO = new TransactionDTO();

        if (matcher.find()) {
            transactionDTO.setCard(cardDTO);
            transactionDTO.setTransactionDetail(matcher.group(2));

            if (Constant.AXIS_CREDIT.equals(matcher.group(4))) {
                transactionDTO.setTransactionType(TransactionType.Credit);
                transactionDTO.setCreditType(populateCreditType(transactionDTO.getTransactionDetail()));
            } else {
                transactionDTO.setTransactionType(TransactionType.Debit);
            }

            transactionDTO.setTransactionAmount(
                    Double.parseDouble(matcher.group(3).replaceAll(Constant.COMMA, Constant.EMPTY_STRING)));
            transactionDTO.setTransactOn(LocalDate.parse(matcher.group(1),
                    DateTimeFormatter.ofPattern(Constant.STATEMENT_DATE_FORMAT)));

            if (Constant.AXIS_DEBIT.equals(matcher.group(6))
                    && Double.parseDouble(matcher.group(5)) > 0) {
                transactionDTO.setAcquiredReward(Double.parseDouble(Constant.HYPHEN + matcher.group(5)));
            } else {
                transactionDTO.setAcquiredReward(Double.parseDouble(matcher.group(5)));
            }
        }

        return transactionDTO;
    }
}
