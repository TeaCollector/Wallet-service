package ru.coffee.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * Basic users with name, password and certain amount
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String name;
    private String password;
    private BigDecimal balance;
}
