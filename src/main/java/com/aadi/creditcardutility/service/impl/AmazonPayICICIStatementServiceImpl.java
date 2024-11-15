package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.TransactionType;
import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.repository.CreditCardRepository;
import com.aadi.creditcardutility.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonPayICICIStatementServiceImpl extends StatementServiceImpl {

    public AmazonPayICICIStatementServiceImpl(CreditCardRepository cardRepository) {
        super(cardRepository);
    }

    @Override
    public List<TransactionDTO> convertStatementToTransactions(StatementFilePOJO statement) {
        return super.convertStatementToTransactions(statement);
    }

    @Override
    public CreditType populateCreditType(String value) {
        CreditType creditType;
        if (Constant.TRANSACTION_ICICI_BILL_PAYMENTS.contains(value)) {
            creditType = CreditType.BillPayment;
        } else if (Constant.TRANSACTION_ICICI_REVERSALS.contains(value)) {
            creditType = CreditType.Reversal;
        } else {
            creditType = CreditType.Refund;
        }
        return creditType;
    }

    @Override
    public TransactionDTO prepareTransactionDTOFromStatementLine(CreditCardDTO cardDTO, String line) {

        Pattern pattern = Pattern.compile(Constant.AMAZON_PAY_ICICI_STATEMENT_REGEX);
        Matcher matcher = pattern.matcher(line);
        TransactionDTO transactionDTO = new TransactionDTO();

        if (matcher.find()) {
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
                    Double.parseDouble(matcher.group(5).replaceAll(Constant.COMMA, Constant.EMPTY_STRING)));
            transactionDTO.setTransactOn(LocalDate.parse(matcher.group(1),
                    DateTimeFormatter.ofPattern(Constant.STATEMENT_DATE_FORMAT)));

            transactionDTO.setAcquiredReward(Double.parseDouble(matcher.group(4)));
        }

        return transactionDTO;
    }
}
