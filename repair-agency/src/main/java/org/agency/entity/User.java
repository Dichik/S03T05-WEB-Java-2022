package org.agency.entity;

import java.math.BigDecimal;

public class User extends Person {

    private Long id;
    private BigDecimal balance;

    private User(UserBuilder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.balance = (builder.balance != null) ? builder.balance : BigDecimal.ZERO;
        this.password = builder.password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void topUp(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void drawback(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public static class UserBuilder {

        private Long id;
        private final String email;
        private BigDecimal balance;
        private String password;

        public UserBuilder(String email) {
            this.email = email;
        }

        public UserBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

}
