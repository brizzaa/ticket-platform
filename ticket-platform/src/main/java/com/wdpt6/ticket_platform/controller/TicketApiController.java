package com.wdpt6.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wdpt6.ticket_platform.model.Ticket;
import com.wdpt6.ticket_platform.repository.TicketRepository;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Ticket> getTicketsByCategoria(@PathVariable Integer categoriaId) {
        return ticketRepository.findByCategoriaId(categoriaId);
    }

    @GetMapping("/stato/{stato}")
    public List<Ticket> getTicketsByStato(@PathVariable String stato) {
        try {
            return ticketRepository.findByStato(Ticket.Status.valueOf(stato.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return List.of(); // Lista vuota se stato non valido
        }
    }

    @GetMapping("/search")
    public List<Ticket> searchTickets(@RequestParam String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return List.of(); // Lista vuota se parametro non valido
        }
        return ticketRepository.findByNomeContainingIgnoreCase(nome);
    }
}