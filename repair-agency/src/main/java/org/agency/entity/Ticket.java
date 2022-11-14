package org.agency.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ticket {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private Long masterId;
    private BigDecimal price;
    private final Timestamp createdAt;

    private Ticket(TicketBuilder builder) {

        this.title = builder.title;
        this.description = builder.description;
        this.price = builder.price;
        this.masterId = builder.masterId;
        this.status = builder.status;

        this.createdAt = new Timestamp(System.currentTimeMillis());
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public static class TicketBuilder {

        private String title;
        private String description;

        private TicketStatus status;
        private Long masterId;
        private BigDecimal price;

        public TicketBuilder(String title, String description) {
            this.title = title;
            this.description = description;

            this.price = null;
            this.masterId = null;
            this.status = TicketStatus.NEW;
        }

        public TicketBuilder setStatus(TicketStatus status) {
            this.status = status;
            return this;
        }

        public TicketBuilder setMasterId(Long masterId) {
            this.masterId = masterId;
            return this;
        }

        public TicketBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }

    }

}
