package com.agency.finalproject.entity.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket { // FIXME create relations for user fields (emails)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    private String title;

    @Size(min = 1, max = 120)
    private String description;

    @Email
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private TicketStatus status = TicketStatus.NEW;

    @Email
    @Size(max = 50)
    private String masterEmail;

    @Builder.Default
    @DecimalMin(value = "0.0")
    private BigDecimal price = BigDecimal.ZERO;

    @Builder.Default
    @Column(updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}
