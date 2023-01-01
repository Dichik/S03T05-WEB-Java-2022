package com.agency.finalproject.entity.feedback;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 255)
    private String text;

    @NotNull(message = "Ticket id can't be null")
    private Long ticketId;

    @NotNull(message = "Ticket id can't be null")
    private String username;

}
