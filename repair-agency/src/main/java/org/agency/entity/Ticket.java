package org.agency.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String userEmail;
    private TicketStatus status;
    private String masterEmail;
    private BigDecimal price;
    private Timestamp createdAt;

    public Ticket() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Ticket(Long id, String title, String description, String userEmail, TicketStatus status, String masterEmail, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userEmail = userEmail;
        this.status = status;
        this.masterEmail = masterEmail;
        this.price = price;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

}
