package org.agency.entity;

public class Manager extends Person {

    private Long id;
    private final String email;
    private final String password;

    private Manager(ManagerBuilder builder) {
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
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public static class ManagerBuilder {

        private final String email;
        private final String password;

        public ManagerBuilder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public Manager build() {
            return new Manager(this);
        }

    }

}
