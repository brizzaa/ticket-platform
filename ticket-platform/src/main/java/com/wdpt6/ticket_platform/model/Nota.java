package com.wdpt6.ticket_platform.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "note")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "testo")
    @Lob
    @NotBlank(message = "Il testo della nota non può essere vuoto")
    private String testo;

    @Column(name = "data_creazione")
    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @NotNull
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "autore_id", nullable = false)
    @NotNull
    private Operatore autore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Operatore getAutore() {
        return autore;
    }

    public void setAutore(Operatore autore) {
        this.autore = autore;
    }

    @PrePersist
    public void prePersist() {
        this.dataCreazione = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return this.testo + " - " + this.dataCreazione + " - " + this.autore.getUsername();
    }
}
