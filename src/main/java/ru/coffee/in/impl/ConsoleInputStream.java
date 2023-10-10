package ru.coffee.in.impl;

import ru.coffee.in.InputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInputStream implements InputStream<BufferedReader> {
    @Override
    public BufferedReader input() throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        return console;
    }
}
