package com.wdpt6.ticket_platform.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "operatori")
public class Operatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    @NotBlank
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "disponibile")
    private boolean disponibile;

    @OneToMany(mappedBy = "operatore")
    private List<Ticket> ticketAssegnati;

    @OneToMany(mappedBy = "autore")
    private List<Nota> noteScritte;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDisponibile() {
        return this.disponibile;
    }

    public boolean getDisponibile() {
        return this.disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public List<Ticket> getTicketAssegnati() {
        return this.ticketAssegnati;
    }

    public void setTicketAssegnati(List<Ticket> ticketAssegnati) {
        this.ticketAssegnati = ticketAssegnati;
    }

    public List<Nota> getNoteScritte() {
        return this.noteScritte;
    }

    public void setNoteScritte(List<Nota> noteScritte) {
        this.noteScritte = noteScritte;
    }

    @PrePersist
    public void prePersist() {
        this.disponibile = true;
    }

    public boolean hasActiveTickets() {
        return this.ticketAssegnati != null &&
                this.ticketAssegnati.stream()
                        .anyMatch(ticket -> ticket.getStato() == Ticket.Status.DA_FARE ||
                                ticket.getStato() == Ticket.Status.IN_CORSO);
    }

    @Override
    public String toString() {
        return "Preso in carico da: " + this.username + ", operatore numero: " + this.id;
    }

}