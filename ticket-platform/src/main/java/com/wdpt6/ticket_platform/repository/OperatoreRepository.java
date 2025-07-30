package com.wdpt6.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wdpt6.ticket_platform.model.Operatore;

public interface OperatoreRepository extends JpaRepository<Operatore, Integer> {

    List<Operatore> findByDisponibile(boolean disponibile);

}
