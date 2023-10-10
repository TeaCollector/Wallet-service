package ru.coffee.in;

import java.io.IOException;

public interface InputStream<T> {
    T input() throws IOException;
}
