package org.agency.entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class Person {

    protected String email;
    protected String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
