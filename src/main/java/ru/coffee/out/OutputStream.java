package ru.coffee.out;


/**
 * Main interface to output stream
 * @param <T> type of output stream
 */
public interface OutputStream<T> {
    void output(T output);
}
