package org.agency.entity;

public class Ticket {

    private Long id;
    private String title;
    private String description;

    public Ticket(
            String title,
            String description
    ) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

}
