package ru.coffee.in.impl;

import ru.coffee.in.InputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for input stream
 */
public class ConsoleInputStream implements InputStream<BufferedReader> {
    /**
     * @return BufferedReader class for input stream
     * @throws IOException
     */
    @Override
    public BufferedReader input() throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        return console;
    }
}
