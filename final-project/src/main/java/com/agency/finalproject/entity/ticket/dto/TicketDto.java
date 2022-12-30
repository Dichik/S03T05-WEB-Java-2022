package com.agency.finalproject.entity.ticket.dto;

import com.agency.finalproject.entity.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private String title;
    private String description;
    private String userEmail;
    private TicketStatus status;
    private String masterEmail;
    private BigDecimal price = BigDecimal.ZERO;
    private Timestamp createdAt;
}
