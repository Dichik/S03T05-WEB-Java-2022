package org.agency.entity;

public class Master extends Person {

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String password;

    private Master(MasterBuilder builder) {
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

    public static class MasterBuilder {

        private String firstName;
        private String secondName;
        private String email;
        private String password;

        public MasterBuilder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public MasterBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public MasterBuilder setSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public Master build() {
            return new Master(this);
        }

    }

}
