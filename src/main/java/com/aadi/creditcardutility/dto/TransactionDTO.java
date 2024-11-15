package com.aadi.creditcardutility.dto;

import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.RewardType;
import com.aadi.creditcardutility.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private CreditCardDTO card;
    private String transactionDetail;
    private TransactionType transactionType;
    private String transactionNo;
    private Double transactionAmount;
    private LocalDate transactOn;
    private CreditType creditType;
    private Double acquiredReward;
    private RewardType rewardType;

    public TransactionDTO(Long id,
                          CreditCardDTO card,
                          String transactionDetail,
                          TransactionType transactionType,
                          String transactionNo,
                          Double transactionAmount,
                          LocalDate transactOn,
                          CreditType creditType,
                          Double acquiredReward) {
        this.id = id;
        this.card = card;
        this.transactionDetail = transactionDetail;
        this.transactionType = transactionType;
        this.transactionNo = transactionNo;
        this.transactionAmount = transactionAmount;
        this.transactOn = transactOn;
        this.creditType = creditType;
        this.acquiredReward = acquiredReward;
    }

    @Override
    public String toString() {
        return "TransactionDTO(id=" + this.id
                + ", card=" + this.card.getDisplayName()
                + ", bank=" + this.card.getBank().getDisplayName()
                + ", transactionDetail=" + this.transactionDetail
                + ", transactionType=" + this.transactionType
                + ", transactionNo=" + this.transactionNo
                + ", transactionAmount=" + this.transactionAmount
                + ", transactOn=" + this.transactOn
                + ", creditType=" + this.creditType
                + ", acquiredReward=" + this.acquiredReward
                + ", rewardType=" + this.rewardType;
    }
}
