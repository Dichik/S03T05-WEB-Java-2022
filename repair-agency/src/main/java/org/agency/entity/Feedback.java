package org.agency.entity;

import lombok.Data;

@Data
public class Feedback {

    private Long id;
    private String text;
    private Long ticketId;
    private Long userId;

}
