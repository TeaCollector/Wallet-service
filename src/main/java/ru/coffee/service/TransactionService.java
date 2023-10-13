package ru.coffee.service;

/**
 * Service for action with transaction.
 */
public interface TransactionService {

    /**
     * Save transaction at Database
     * @param transactionId uniq transaction id
     * @return the same transaction
     */
    String addTransaction(String transactionId);

    /**
     * Verify transaction is valid or not
     * @param transactionId uniq transaction id
     * @return true if transaction is valid, otherwise false
     */
    boolean validTransaction(String transactionId);
}
