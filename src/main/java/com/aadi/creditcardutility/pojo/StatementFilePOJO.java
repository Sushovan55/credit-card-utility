package com.aadi.creditcardutility.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementFilePOJO {
    private String filePath;
    private String creditCardName;
    private String bankName;
}
