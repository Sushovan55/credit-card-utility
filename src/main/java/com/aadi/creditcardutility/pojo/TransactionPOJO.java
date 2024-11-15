package com.aadi.creditcardutility.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPOJO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Month month;
    private Integer year;
    private String creditCardName;
    private String bankName;
}
