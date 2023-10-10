package ru.coffee.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationDetail {
    private String beginOperationTIme;
    private BigDecimal action;

    @Override
    public String toString() {
        return "beginOperationTIme='" + beginOperationTIme + '\'' +
               ", action= " + action;
    }
}
