package com.wdpt6.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wdpt6.ticket_platform.model.Ticket;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
        List<Ticket> findByNomeContainingIgnoreCase(String nome);

}