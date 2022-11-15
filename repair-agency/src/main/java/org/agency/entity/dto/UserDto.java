package org.agency.entity.dto;

import java.math.BigDecimal;

public class UserDto {

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private BigDecimal balance;

    public BigDecimal topUp(BigDecimal amount) {
        return this.balance.add(amount);
    }
}
