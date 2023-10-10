package ru.coffee.out.impl;

import ru.coffee.out.OutputStream;

public class ConsoleOutputStream implements OutputStream {

    @Override
    public void output(String message) {
        System.out.print(message);
    }
}
