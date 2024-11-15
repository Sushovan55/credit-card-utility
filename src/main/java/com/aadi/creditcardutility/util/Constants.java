package com.aadi.creditcardutility.util;

import java.util.List;

public class Constants {
    public static final String COMMA = ",";
    public static final String EMPTY_STRING = "";
    public static final String HYPHEN = "-";
    public static final String STATEMENT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEBIT = "Debit";
    public static final String CREDIT = "Credit";

    //ICICI Constants
    public static final String BANK_ICICI = "ICICI";
    public static final String CREDIT_CARD_AMAZON_PAY_ICICI = "AmazonPay ICICI";
    public static final String AMAZON_PAY_ICICI_STATEMENT_REGEX = "(\\d{2}/\\d{2}/\\d{4})\\s(\\d+)\\s(.+)\\s(-\\d+|\\d+)\\s(\\d+,\\d+.\\d{2}|\\d+.\\d{2})\\s*(\\w*)";
    public static final List<String> TRANSACTION_ICICI_BILL_PAYMENTS = List.of(
            "INFINITY PAYMENT RECEIVED, THANK YOU",
            "NEFT PAYMENT RECEIVED",
            "UPI Payment Received",
            "CLICK TO PAY PAYMENT RECEIVED",
            "BBPS Payment received"
    );

    public static final List<String> TRANSACTION_ICICI_REVERSALS = List.of(
            "Reversal of fuel Surcharge",
            "INSTANT EMI OFFUS CONVERSION",
            "Reversal Interest Amount Amortization - <1/18>",
            "IGST-Rev-CI@18%",
            "EMI PROCESSING FEE REVERSAL",
            "IGST-Rev-CI@18%"
    );

    //Axis Constants
    public static final String BANK_AXIS = "Axis";
    public static final String FLIPKART_AXIS_STATEMENT_REGEX = "(\\d{2}/\\d{2}/\\d{4})\\s(.+)\\s(\\d+,\\d+.\\d{2}|\\d+.\\d{2})\\s(Dr|Cr)\\s(\\d+.\\d{2})\\s(Dr|Cr)";
    public static final String CREDIT_CARD_FLIPKART_AXIS = "Flipkart Axis";
    public static final String CREDIT_CARD_AIRTEL_AXIS = "Airtel Axis";
    public static final String AXIS_CREDIT = "Cr";
    public static final String AXIS_DEBIT = "Dr";
    public static final String AIRTEL_AXIS_STATEMENT_REGEX = "(\\d{2}/\\d{2}/\\d{4})\\s(.+)\\s(\\d+,\\d+.\\d{2}|\\d+.\\d{2})\\s(Dr|Cr)\\s*(\\d*)";
    public static final List<String> TRANSACTION_AXIS_BILL_PAYMENTS = List.of(
            "IMPS PAYMENT#",
            "ONLINE PAYMENT",
            "NEFT PAYMENT RECEIVED",
            "EURONETCHENNAIIN",
            "UPI PAYMENT RECEIVED",
            "BBPS PAYMENT RECEIVED"
    );
    public static final String TRANSACTION_AIRTEL_PAYMENTS_BANK = "AIRTEL PAYMENTS BANK";
    public static final String TRANSACTION_AXIS_CASHBACK = "CASHBACK CREDIT";
    public static final String TRANSACTION_ZOMATO = "ZOMATO";
    public static final String TRANSACTION_BIG_BASKET = "INNOVATIVE RETAIL";
    public static final List<String> TRANSACTION_NON_ELIGIBLE = List.of(
            "JOINING FEE",
            "GST"
    );
    public static final String MERCHANT_UTILITY = "UTILITIES";
    public static final String MERCHANT_DEPT_STORE = "DEPT STORES";
    public static final String MERCHANT_FOOD = "FOOD PRODUCTS";
    public static final String MERCHANT_RESTAURANT = "RESTAURANTS";
    public static final List<Double> AMOUNT_INTERNET_BILL = List.of(
            1178.82
    );
    public static final Double LIMIT_25 = 250.0;
    public static final Double LIMIT_UTILITY_10 = 250.0;
    public static final Double LIMIT_FOOD_10 = 500.0;


}
