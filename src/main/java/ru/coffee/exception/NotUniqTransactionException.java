package ru.coffee.exception;

public class NotUniqTransactionException extends Exception {
    public NotUniqTransactionException(String message) {
        super(message);
    }
}
