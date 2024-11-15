package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.pojo.StatementFilePOJO;

public interface StatementFactory {
    StatementService getStatementService(StatementFilePOJO statement) throws ClassNotFoundException;
}
