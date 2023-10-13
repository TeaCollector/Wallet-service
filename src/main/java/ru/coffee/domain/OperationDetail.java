package ru.coffee.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Class with basic operation detail
 */
@Data
public class OperationDetail {
    /**
     * Time when operation begin
     */
    private String beginOperationTIme;

    /**
     * User action adding some money or withdraw it
     */
    private BigDecimal action;

    @Override
    public String toString() {
        return "beginOperationTIme='" + beginOperationTIme + '\'' +
               ", action= " + action;
    }
}
