package com.wdpt6.ticket_platform.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    // fix
    @Column(name = "Disponibile", nullable = false)
    private Boolean Disponibile = true;

    @OneToMany(mappedBy = "operatore")
    private List<Ticket> ticketAssegnati;

    @OneToMany(mappedBy = "autore")
    private List<Nota> noteScritte;

}