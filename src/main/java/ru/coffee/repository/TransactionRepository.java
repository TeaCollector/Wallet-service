package ru.coffee.repository;

public interface TransactionRepository {

    /**
     * @param transactionId uniq transaction id for save in db
     */
    String addTransactionId(String transactionId);

    /**
     *
     * @param transactionId uniq transaction id
     * @return true if the same transaction was find, otherwise false
     */
    boolean isValidTransaction(String transactionId);
}
