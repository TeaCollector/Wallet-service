package ru.coffee.service.impl;

import ru.coffee.repository.TransactionRepository;
import ru.coffee.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public String addTransaction(String transactionId) {
        transactionRepository.addTransactionId(transactionId);
        return transactionId;
    }

    @Override
    public boolean validTransaction(String transactionId) {
        return transactionRepository.isValidTransaction(transactionId);
    }
}
