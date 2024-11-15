package com.aadi.creditcardutility.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO {
    private Integer id;
    private String name;
    private String displayName;
    private BankDTO bank;
    private Integer cardDetailId;
    private Double monthlyLimit;
    private Double joiningFee;
    private Double annualFee;
    private Boolean loungeAccess;
    private LocalDate activatedOn;
    private Integer billingStart;
    private Integer billingEnd;
    private Boolean status;
}
