package ru.coffee.out.impl;

import ru.coffee.out.OutputStream;

/**
 * Class for console output stream
 */
public class ConsoleOutputStream implements OutputStream<String> {

    /**
     * Output method
     * @param message for console output
     */
    @Override
    public void output(String message) {
        System.out.print(message);
    }
}