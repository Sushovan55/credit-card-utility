package com.aadi.creditcardutility.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String displayName;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "card_detail_id")
    private Integer cardDetailId;

    @NotNull
    @Column(nullable = false)
    private Double monthlyLimit;

    private Double joiningFee;

    private Double annualFee;

    private Boolean loungeAccess;

    private LocalDate activatedOn;

    @NotNull
    @Column(nullable = false)
    private Integer billingStart;

    @NotNull
    @Column(nullable = false)
    private Integer billingEnd;

    private Boolean status;
}
