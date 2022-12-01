package org.agency.entity;

public class Manager extends Person {

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String password;

    private Manager(ManagerBuilder builder) {
        this.firstName = builder.firstName;
        this.secondName = builder.secondName;
        this.email = builder.email;
        this.password = builder.password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getSecondName() {
        return secondName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public static class ManagerBuilder {

        private String firstName;
        private String secondName;
        private String email;
        private String password;

        public ManagerBuilder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public ManagerBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ManagerBuilder setSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public Manager build() {
            return new Manager(this);
        }

    }

}
