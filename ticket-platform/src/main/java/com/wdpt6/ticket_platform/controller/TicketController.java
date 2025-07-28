package com.wdpt6.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wdpt6.ticket_platform.model.Ticket;
import com.wdpt6.ticket_platform.repository.TicketRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public String listaTicket(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "tickets/index";
    }

    @GetMapping("/{id}")
    public String dettaglioTicket(@PathVariable Integer id, Model model) {
        Ticket ticket = ticketRepository.findById(id).get();
        model.addAttribute("ticket", ticket);
        return "tickets/show";
    }

    @GetMapping("/create")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "tickets/create";
    }

    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tickets/create";
        }
        ticketRepository.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("ticket", ticketRepository.findById(id).get());
        return "tickets/edit";
    }

    @PostMapping("/edit/{id}")
    public String post(Model model, @PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket formTicket,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tickets/edit";
        }
        ticketRepository.save(formTicket);
        return "redirect:/ticket";
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        ticketRepository.deleteById(id);
        return "redirect:/tickets";
    }
}
