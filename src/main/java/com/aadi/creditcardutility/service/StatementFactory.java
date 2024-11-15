package com.aadi.creditcardutility.service;

import com.aadi.creditcardutility.pojo.StatementFilePOJO;
import jakarta.persistence.EntityNotFoundException;

public interface StatementFactory {

    StatementService getStatementService(StatementFilePOJO statement) throws EntityNotFoundException;
}
