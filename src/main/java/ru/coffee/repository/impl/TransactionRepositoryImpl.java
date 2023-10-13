package ru.coffee.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.coffee.exception.NotUniqTransactionException;
import ru.coffee.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Logger logger = LogManager.getLogger(TransactionRepository.class.getName());
    private final List<String> transactionList = new ArrayList<>();


    @Override
    public String addTransactionId(String transactionId) {
        logger.info("Transaction with id {} was added at database", transactionId);
        transactionList.add(transactionId);
        return transactionId;
    }

    public boolean isValidTransaction(String transactionId) {
        if (!transactionList.contains(transactionId)) {
            logger.info("Transaction was saved.");
            return true;
        } else {
            try {
                logger.error("Transaction not uniq");
                throw new NotUniqTransactionException("Transaction not uniq.");
            } catch (NotUniqTransactionException e) {
                return false;
            }
        }
    }
}
