package org.agency.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class User {

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private BigDecimal balance;

    public BigDecimal topUp(BigDecimal amount) {
        return this.balance.add(amount);
    }
}
