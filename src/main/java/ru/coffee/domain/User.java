package ru.coffee.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
public class User {
    private String name;
    private String password;
    private BigDecimal balance;
}
