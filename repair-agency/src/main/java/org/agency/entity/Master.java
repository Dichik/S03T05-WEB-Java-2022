package org.agency.entity;

public class Master extends Person {

    private Long id;
    private final String email;
    private final String password;

    private Master(MasterBuilder builder) {
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

    public static class MasterBuilder {

        private final String email;
        private final String password;

        public MasterBuilder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public Master build() {
            return new Master(this);
        }

    }

}
