package com.aadi.creditcardutility.entity;

import com.aadi.creditcardutility.enums.CreditType;
import com.aadi.creditcardutility.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cc_transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credit_card_id", nullable = false)
    private CreditCard card;

    private String transactionDetail;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private String transactionNo;

    @Column(nullable = false)
    private Double transactionAmount;

    private LocalDate transactOn;

    @Enumerated(EnumType.STRING)
    private CreditType creditType;

    private Double acquiredReward;
}
