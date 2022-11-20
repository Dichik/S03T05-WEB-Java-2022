package org.agency.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public class User {

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private BigDecimal balance;
    private String password;

    private User(UserBuilder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.secondName = builder.secondName;
        this.email = builder.email;
        this.balance = builder.balance;
        this.password = builder.password;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal topUp(BigDecimal amount) {
        return this.balance.add(amount);
    }

    public static class UserBuilder {

        private Long id;
        private String firstName;
        private String secondName;
        private String email;
        private BigDecimal balance;
        private String password;

        public UserBuilder(String email) {
            this.email = email;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setSecondName(String secondName) {
            this.secondName = secondName;
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
