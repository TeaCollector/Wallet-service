package ru.coffee.in;

import java.io.IOException;

/**
 * Main interface to input stream
 * @param <T> type of input stream
 */
public interface InputStream<T> {
    T input() throws IOException;
}
