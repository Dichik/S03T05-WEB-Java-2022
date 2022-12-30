package com.agency.finalproject.entity.ticket;

import lombok.*;

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
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TicketStatus status;

    @Null
    @Email
    @Size(max = 50)
    private String masterEmail;

    @Builder.Default
    @Min(0) @Max(1000)
    private BigDecimal price = BigDecimal.ZERO;

    @Builder.Default
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}
