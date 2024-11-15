package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import com.aadi.creditcardutility.service.StatementFactory;
import com.aadi.creditcardutility.service.StatementService;
import com.aadi.creditcardutility.util.Constant;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StatementFactoryImpl implements StatementFactory {

    private AmazonPayICICIStatementServiceImpl amazonPayICICIStatementService;
    private FlipkartAxisStatementServiceImpl flipkartAxisStatementService;
    private AirtelAxisStatementServiceImpl airtelAxisStatementService;

    @Override
    public StatementService getStatementService(StatementFilePOJO statement) throws EntityNotFoundException {
        switch (statement.getCreditCardName()) {
            case Constant.CREDIT_CARD_AMAZON_PAY_ICICI -> {
                return amazonPayICICIStatementService;
            }
            case Constant.CREDIT_CARD_FLIPKART_AXIS -> {
                return flipkartAxisStatementService;
            }
            case Constant.CREDIT_CARD_AIRTEL_AXIS -> {
                return airtelAxisStatementService;
            }
            default -> throw new EntityNotFoundException("Invalid credit card name: " + statement.getCreditCardName());
        }
    }
}
