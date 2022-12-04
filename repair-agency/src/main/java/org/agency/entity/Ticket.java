package org.agency.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ticket {

    private Long id;
    private final String title;
    private final String description;
    private final String userEmail;
    private TicketStatus status;
    private String masterEmail;
    private BigDecimal price;
    private final Timestamp createdAt;

    private Ticket(TicketBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.userEmail = builder.userEmail;
        this.price = builder.price;
        this.masterEmail = builder.masterId;
        this.status = builder.status;

        this.createdAt = builder.createdAt != null ? builder.createdAt : new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getMasterEmail() {
        return masterEmail;
    }

    public void setMasterEmail(String masterEmail) {
        this.masterEmail = masterEmail;
    }

    public BigDecimal getPrice() {
        return price == null ? BigDecimal.ZERO : price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", status=" + status +
                ", masterEmail=" + masterEmail +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }

    public static class TicketBuilder {

        private Long id;
        private final String title;
        private final String description;
        private final String userEmail;

        private TicketStatus status;
        private String masterId;
        private BigDecimal price;
        private Timestamp createdAt;

        public TicketBuilder(String title, String description, String userEmail) {
            this.title = title;
            this.description = description;
            this.userEmail = userEmail;

            this.status = TicketStatus.NEW;
        }

        public TicketBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public TicketBuilder setStatus(TicketStatus status) {
            this.status = status;
            return this;
        }

        public TicketBuilder setMasterId(String masterId) {
            this.masterId = masterId;
            return this;
        }

        public TicketBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public TicketBuilder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }

    }

}
