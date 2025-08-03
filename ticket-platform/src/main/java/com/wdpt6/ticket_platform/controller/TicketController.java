package com.wdpt6.ticket_platform.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.wdpt6.ticket_platform.model.Categoria;
import com.wdpt6.ticket_platform.model.Nota;
import com.wdpt6.ticket_platform.model.Operatore;
import com.wdpt6.ticket_platform.model.Ticket;
import com.wdpt6.ticket_platform.repository.CategoriaRepository;
import com.wdpt6.ticket_platform.repository.NotaRepository;
import com.wdpt6.ticket_platform.repository.OperatoreRepository;
import com.wdpt6.ticket_platform.repository.TicketRepository;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    // dependency injection
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OperatoreRepository operatoreRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private NotaRepository notaRepository;

    @GetMapping // get su ("tickets") index
    public String listaTicket(Model model, @RequestParam(required = false) String search) {
        List<Ticket> tickets;

        if (search != null) {
            tickets = ticketRepository.findByNomeContainingIgnoreCase(search);
        } else {
            // trovo tutti i tickets
            tickets = ticketRepository.findAll();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("search", search);

        return "tickets/index";
    }

    // show
    @GetMapping("/{id}")
    public String dettaglioTicket(@PathVariable Integer id, Model model) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            return "redirect:/tickets";
        }

        Ticket ticket = ticketOpt.get();
        model.addAttribute("ticket", ticket);
        model.addAttribute("nuovaNota", new Nota());
        return "tickets/show";
    }

    // create
    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createTicketForm(Model model) {
        List<Categoria> categorie = categoriaRepository.findAll();
        // solo gli operatori disponibili possono essere assegnati
        List<Operatore> operatori = operatoreRepository.findByDisponibile(true);

        model.addAttribute("categorie", categorie);
        model.addAttribute("operatori", operatori);
        model.addAttribute("ticket", new Ticket());
        return "tickets/create";
    }

    // ascolto form di create
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String store(Model model, @Valid @ModelAttribute("ticket") Ticket ticket,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<Categoria> categorie = categoriaRepository.findAll();
            // Solo operatori disponibili possono essere assegnati
            List<Operatore> operatori = operatoreRepository.findByDisponibile(true);
            model.addAttribute("categorie", categorie);
            model.addAttribute("operatori", operatori);
            return "tickets/create";
        }
        // validazione garantita dal fatto che mostriamo solo operatori
        // disponibili

        ticketRepository.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String edit(Model model, @PathVariable Integer id) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);

        if (ticketOpt.isEmpty()) {
            return "redirect:/tickets";
        }

        List<Categoria> categorie = categoriaRepository.findAll();
        List<Operatore> operatori = operatoreRepository.findByDisponibile(true);

        model.addAttribute("ticket", ticketOpt.get());
        model.addAttribute("categorie", categorie);
        model.addAttribute("operatori", operatori);
        model.addAttribute("stati", Ticket.Status.values());
        return "tickets/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String update(Model model, @PathVariable Integer id,
            @Valid @ModelAttribute("ticket") Ticket formTicket,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<Categoria> categorie = categoriaRepository.findAll();
            List<Operatore> operatori = operatoreRepository.findByDisponibile(true);
            model.addAttribute("categorie", categorie);
            model.addAttribute("operatori", operatori);
            model.addAttribute("stati", Ticket.Status.values());
            return "tickets/edit";
        }

        formTicket.setId(id);
        ticketRepository.save(formTicket);

        return "redirect:/tickets/" + id;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable Integer id) {

        // esiste il ticket??
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();

            // elimina prima note
            if (ticket.getNote() != null && !ticket.getNote().isEmpty()) {
                notaRepository.deleteAll(ticket.getNote());
            }
            // poi elimina il ticket
            ticketRepository.deleteById(id);
        }

        return "redirect:/tickets";
    }

    // aggiorna stato ticket
    @PostMapping("/{id}/update-status")
    public String updateStatus(@PathVariable Integer id, @RequestParam String stato) {

        Optional<Ticket> ticketOpt = ticketRepository.findById(id);

        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setStato(Ticket.Status.valueOf(stato));
            ticketRepository.save(ticket);
        }
        return "redirect:/tickets/" + id;
    }

}