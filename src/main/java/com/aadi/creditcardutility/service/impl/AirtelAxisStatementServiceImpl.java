package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.RewardType;
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
public class AirtelAxisStatementServiceImpl extends StatementServiceImpl {

    public AirtelAxisStatementServiceImpl(CreditCardRepository cardRepository) {
        super(cardRepository);
    }

    @Override
    public List<TransactionDTO> convertStatementToTransactions(StatementFilePOJO statement) {

        List<TransactionDTO> transactionDTOs = super.convertStatementToTransactions(statement);
        applyMaxLimitOnReward(transactionDTOs);

        return transactionDTOs;
    }

    @Override
    public CreditType populateCreditType(String transactionDetail) {
        CreditType creditType;
        for (String billPayment : Constant.TRANSACTION_AXIS_BILL_PAYMENTS) {
            if (transactionDetail.startsWith(billPayment)) {
                return CreditType.BillPayment;
            }
        }
        if (transactionDetail.startsWith(Constant.TRANSACTION_AXIS_CASHBACK)) {
            creditType = CreditType.Cashback;
        } else {
            creditType = CreditType.Refund;
        }
        return creditType;
    }

    @Override
    public TransactionDTO prepareTransactionDTOFromStatementLine(CreditCardDTO cardDTO, String line) {

        Pattern pattern = Pattern.compile(Constant.AIRTEL_AXIS_STATEMENT_REGEX);
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
            transactionDTO.setAcquiredReward(populateReward(transactionDTO));
        }

        return transactionDTO;
    }

    private Double populateReward(TransactionDTO transactionDTO) {
        double reward = 0.0;

        if (TransactionType.Debit == transactionDTO.getTransactionType()) {

            if (transactionDTO.getTransactionDetail().startsWith(Constant.TRANSACTION_AIRTEL_PAYMENTS_BANK)
                    && transactionDTO.getTransactionDetail().endsWith(Constant.MERCHANT_UTILITY)) {

                //25% Discount
                if (Constant.AMOUNT_INTERNET_BILL.contains(transactionDTO.getTransactionAmount())) {
                    transactionDTO.setRewardType(RewardType.AirtelAxisInternet);
                    reward = Math.floor(transactionDTO.getTransactionAmount()
                            * RewardType.AirtelAxisInternet.getReward());
                }

                //10% Discount on Utility
                else {
                    transactionDTO.setRewardType(RewardType.AirtelAxisUtility);
                    reward = Math.floor(transactionDTO.getTransactionAmount()
                            * RewardType.AirtelAxisUtility.getReward());
                }
            }

            //10% Discount on Swiggy+Zomato+BigBasket
            //Zomato transaction
            else if ((transactionDTO.getTransactionDetail().contains(Constant.TRANSACTION_ZOMATO)
                    && (transactionDTO.getTransactionDetail().endsWith(Constant.MERCHANT_FOOD)
                        ||transactionDTO.getTransactionDetail().endsWith(Constant.MERCHANT_RESTAURANT)))
                    //BigBasket transaction
                    || (transactionDTO.getTransactionDetail().contains(Constant.TRANSACTION_BIG_BASKET)
                        && transactionDTO.getTransactionDetail().endsWith(Constant.MERCHANT_DEPT_STORE))
                    //TODO - Add Swiggy transaction logic
            ) {
                transactionDTO.setRewardType(RewardType.AirtelAxisFood);
                reward = Math.floor(transactionDTO.getTransactionAmount()
                        * RewardType.AirtelAxisFood.getReward());
            }

            //1% Discount
            else if (!Constant.TRANSACTION_NON_ELIGIBLE.contains(transactionDTO.getTransactionDetail())) {
                transactionDTO.setRewardType(RewardType.AirtelAxisBase);
                reward = Math.floor(transactionDTO.getTransactionAmount()
                        * RewardType.AirtelAxisBase.getReward());
            }
        }
        return reward;
    }

    public void applyMaxLimitOnReward(List<TransactionDTO> transactionDTOs) {

        double rem25Reward = Constant.LIMIT_25;
        double rem10UtilityReward = Constant.LIMIT_UTILITY_10;
        double rem10FoodReward = Constant.LIMIT_FOOD_10;

        for (TransactionDTO transactionDTO : transactionDTOs) {
            if (RewardType.AirtelAxisInternet == transactionDTO.getRewardType()) {
                if (rem25Reward > transactionDTO.getAcquiredReward()) {
                    rem25Reward -= transactionDTO.getAcquiredReward();
                } else if (transactionDTO.getAcquiredReward() > 0) {
                    transactionDTO.setAcquiredReward(rem25Reward);
                    rem25Reward = 0.0;
                }

            } else if (RewardType.AirtelAxisUtility == transactionDTO.getRewardType()) {
                if (rem10UtilityReward > transactionDTO.getAcquiredReward()) {
                    rem10UtilityReward -= transactionDTO.getAcquiredReward();
                } else if (transactionDTO.getAcquiredReward() > 0) {
                    transactionDTO.setAcquiredReward(rem10UtilityReward);
                    rem10UtilityReward = 0.0;
                }

            } else if (RewardType.AirtelAxisFood == transactionDTO.getRewardType()) {
                if (rem10FoodReward > transactionDTO.getAcquiredReward()) {
                    rem10FoodReward -= transactionDTO.getAcquiredReward();
                } else if (transactionDTO.getAcquiredReward() > 0) {
                    transactionDTO.setAcquiredReward(rem10FoodReward);
                    rem10FoodReward = 0.0;
                }
            }
        }
    }
}
