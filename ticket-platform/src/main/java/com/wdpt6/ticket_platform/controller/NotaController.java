package com.wdpt6.ticket_platform.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wdpt6.ticket_platform.model.Nota;
import com.wdpt6.ticket_platform.model.Operatore;
import com.wdpt6.ticket_platform.model.Ticket;
import com.wdpt6.ticket_platform.repository.NotaRepository;
import com.wdpt6.ticket_platform.repository.OperatoreRepository;
import com.wdpt6.ticket_platform.repository.TicketRepository;
import com.wdpt6.ticket_platform.security.DatabaseUserDetails;

@Controller
@RequestMapping("/note")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OperatoreRepository operatoreRepository;

    // ascolto il form
    @PostMapping("/ticket/{ticketId}")
    public String addNota(@PathVariable Integer ticketId,
            @RequestParam String testo,
            Authentication authentication) {

        // validazione
        if (testo == null || testo.trim().isEmpty()) {
            return "redirect:/tickets/" + ticketId;
        }

        // verifico esistenza ticket
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return "redirect:/tickets";
        }

        // l'operatore loggato
        DatabaseUserDetails userDetails = (DatabaseUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<Operatore> operatoreOpt = operatoreRepository.findByUsername(username);
        if (operatoreOpt.isEmpty()) {
            return "redirect:/tickets/" + ticketId;
        }

        // crea e salva la nota
        Nota nota = new Nota();
        nota.setTesto(testo.trim());
        nota.setTicket(ticketOpt.get());
        nota.setAutore(operatoreOpt.get());
        notaRepository.save(nota);

        return "redirect:/tickets/" + ticketId;
    }
}