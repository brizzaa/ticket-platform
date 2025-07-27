package com.wdpt6.ticket_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descrizione")
    @NotBlank
    @Lob
    private String descrizione;

    private enum StatusEnum {
        DA_FARE,
        IN_CORSO,
        COMPLETATO
    }

    // testare
    @Column(name = "stato")
    @NotBlank
    @Enumerated(EnumType.STRING)
    private StatusEnum stato;

    @ManyToOne
    @JoinColumn(name = "operatore_id")
    private Operatore operatore;

    @OneToMany(mappedBy = "ticket")
    private List<Nota> note;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public StatusEnum getStato() {
        return this.stato;
    }

    public void setStato(StatusEnum stato) {
        this.stato = stato;
    }

    public Operatore getOperatore() {
        return this.operatore;
    }

    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

    public List<Nota> getNote() {
        return this.note;
    }

    public void setNota(List<Nota> note) {
        this.note = note;
    }

}