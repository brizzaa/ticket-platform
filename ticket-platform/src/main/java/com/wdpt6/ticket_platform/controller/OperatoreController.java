package com.wdpt6.ticket_platform.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wdpt6.ticket_platform.model.Operatore;
import com.wdpt6.ticket_platform.model.Ticket;
import com.wdpt6.ticket_platform.repository.OperatoreRepository;
import com.wdpt6.ticket_platform.repository.TicketRepository;
import com.wdpt6.ticket_platform.security.DatabaseUserDetails;

import java.util.List;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {

    @Autowired
    private OperatoreRepository operatoreRepository;
    @Autowired
    private TicketRepository ticketRepository;

    // profilo operatore/admin
    @GetMapping("/profilo")
    public String profilo(Model model, Authentication authentication) {

        // operatore loggato
        DatabaseUserDetails userDetails = (DatabaseUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<Operatore> operatoreOpt = operatoreRepository.findByUsername(username);
        if (operatoreOpt.isEmpty()) {
            return "redirect:/";
        }

        Operatore operatore = operatoreOpt.get();
        model.addAttribute("operatore", operatore);

        // aggiungo i ticket assegnati all'operatore
        List<Ticket> ticketAssegnati = ticketRepository.findByOperatoreId(operatore.getId());
        model.addAttribute("ticketAssegnati", ticketAssegnati);

        // statistiche sui ticket
        int ticketAttivi = 0;
        int ticketCompletati = 0;

        for (Ticket ticket : ticketAssegnati) {
            if (ticket.getStato() == Ticket.Status.DA_FARE || ticket.getStato() == Ticket.Status.IN_CORSO) {
                ticketAttivi++;
            } else if (ticket.getStato() == Ticket.Status.COMPLETATO) {
                ticketCompletati++;
            }
        }

        model.addAttribute("ticketTotali", ticketAssegnati.size());
        model.addAttribute("ticketAttivi", ticketAttivi);
        model.addAttribute("ticketCompletati", ticketCompletati);

        return "operatore/profilo";
    }

    @PostMapping("/toggle-disponibilita")
    public String toggleDisponibilita(@RequestParam boolean disponibile,
            Authentication authentication) {

        // operatore loggato
        DatabaseUserDetails userDetails = (DatabaseUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<Operatore> operatoreOpt = operatoreRepository.findByUsername(username);

        if (operatoreOpt.isEmpty()) {
            return "redirect:/operatore/profilo";
        }

        Operatore operatore = operatoreOpt.get();

        // se vuole diventare non disponibile, controlla i ticket
        // attivi
        if (!disponibile && operatore.hasActiveTickets()) {

            // errore se ha tickets in corso (gestito nel template)
            return "redirect:/operatore/profilo?error=ticket-attivi";
        }

        // aggionra lo stato se non ha tickets
        operatore.setDisponibile(disponibile);
        operatoreRepository.save(operatore);

        return "redirect:/operatore/profilo?success=stato-aggiornato";
    }
}