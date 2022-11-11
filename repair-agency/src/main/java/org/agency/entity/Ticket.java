package org.agency.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class Ticket {

    private Long id;
    private String title;
    private String description;
    private String status; // TODO enum in databases research
    private Long masterId;
    private BigDecimal price;
    private LocalTime createdAt;

}
