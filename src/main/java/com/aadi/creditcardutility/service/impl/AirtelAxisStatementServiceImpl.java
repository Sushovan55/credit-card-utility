package com.aadi.creditcardutility.service.impl;

import com.aadi.creditcardutility.dto.CreditCardDTO;
import com.aadi.creditcardutility.dto.TransactionDTO;
import com.aadi.creditcardutility.entity.CreditCard;
import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.RewardType;
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
public class AirtelAxisStatementServiceImpl implements StatementService {

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

                Pattern pattern = Pattern.compile(Constants.AIRTEL_AXIS_STATEMENT_REGEX);
                Matcher matcher = pattern.matcher(st);

                if (matcher.find()) {
                    transactionDTOs.add(prepareTransactionDTOFromStatementLine(matcher, cardDTO));
                }
            }

            applyMaxLimitOnReward(transactionDTOs);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return transactionDTOs;
    }

    @Override
    public CreditType populateCreditType(String transactionDetail) {
        CreditType creditType;
        for (String billPayment : Constants.TRANSACTION_AXIS_BILL_PAYMENTS) {
            if (transactionDetail.startsWith(billPayment)) {
                return CreditType.BillPayment;
            }
        }
        if (transactionDetail.startsWith(Constants.TRANSACTION_AXIS_CASHBACK)) {
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
        transactionDTO.setAcquiredReward(populateReward(transactionDTO));

        return transactionDTO;
    }

    private Double populateReward(TransactionDTO transactionDTO) {
        double reward = 0.0;

        if (TransactionType.Debit == transactionDTO.getTransactionType()) {

            if (transactionDTO.getTransactionDetail().startsWith(Constants.TRANSACTION_AIRTEL_PAYMENTS_BANK)
                    && transactionDTO.getTransactionDetail().endsWith(Constants.MERCHANT_UTILITY)) {

                //25% Discount
                if (Constants.AMOUNT_INTERNET_BILL.contains(transactionDTO.getTransactionAmount())) {
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
            else if ((transactionDTO.getTransactionDetail().contains(Constants.TRANSACTION_ZOMATO)
                    && (transactionDTO.getTransactionDetail().endsWith(Constants.MERCHANT_FOOD)
                        ||transactionDTO.getTransactionDetail().endsWith(Constants.MERCHANT_RESTAURANT)))
                    //BigBasket transaction
                    || (transactionDTO.getTransactionDetail().contains(Constants.TRANSACTION_BIG_BASKET)
                        && transactionDTO.getTransactionDetail().endsWith(Constants.MERCHANT_DEPT_STORE))
                    //TODO - Add Swiggy transaction logic
            ) {
                transactionDTO.setRewardType(RewardType.AirtelAxisFood);
                reward = Math.floor(transactionDTO.getTransactionAmount()
                        * RewardType.AirtelAxisFood.getReward());
            }

            //1% Discount
            else if (!Constants.TRANSACTION_NON_ELIGIBLE.contains(transactionDTO.getTransactionDetail())) {
                transactionDTO.setRewardType(RewardType.AirtelAxisBase);
                reward = Math.floor(transactionDTO.getTransactionAmount()
                        * RewardType.AirtelAxisBase.getReward());
            }
        }
        return reward;
    }

    private List<TransactionDTO> applyMaxLimitOnReward(List<TransactionDTO> transactionDTOs) {

        double rem25Reward = Constants.LIMIT_25;
        double rem10UtilityReward = Constants.LIMIT_UTILITY_10;
        double rem10FoodReward = Constants.LIMIT_FOOD_10;

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

        return transactionDTOs;
    }
}
