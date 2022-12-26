package com.agency.finalproject.repository.ticket;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserEmail(String userEmail);

    List<Ticket> findByMasterEmail(String masterEmail);

    List<Ticket> findByStatus(TicketStatus status);
}
