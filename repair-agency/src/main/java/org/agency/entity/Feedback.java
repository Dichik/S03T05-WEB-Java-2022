package org.agency.entity;

public class Feedback {

    private Long id;
    private String text;
    private Long ticketId;
    private String userEmail;

    private Feedback(FeedbackBuilder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.ticketId = builder.ticketId;
        this.userEmail = builder.userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public static class FeedbackBuilder {
        private final String text;
        private Long id;
        private Long ticketId;
        private String userEmail;

        public FeedbackBuilder(String text) {
            this.text = text;
        }

        public FeedbackBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public FeedbackBuilder setTicketId(Long ticketId) {
            this.ticketId = ticketId;
            return this;
        }

        public FeedbackBuilder setUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Feedback build() {
            return new Feedback(this);
        }

    }

}
