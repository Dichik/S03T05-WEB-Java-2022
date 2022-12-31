package com.agency.finalproject.entity.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 120)
    private String description;

    @Null
    @Email
    @Size(max = 50)
    private String userEmail; // FIXME relation

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private TicketStatus status = TicketStatus.NEW; // FIXME create table for statuses

    @Null
    @Email
    @Size(max = 50)
    private String masterEmail; // FIXME create relation

    @Builder.Default
    @Min(0)
    @Max(1000)
    private BigDecimal price = BigDecimal.ZERO;

    @Builder.Default
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}
