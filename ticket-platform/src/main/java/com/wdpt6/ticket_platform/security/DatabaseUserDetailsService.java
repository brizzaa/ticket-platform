package com.wdpt6.ticket_platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wdpt6.ticket_platform.model.Operatore;
import com.wdpt6.ticket_platform.repository.OperatoreRepository;

@Service
// Dice a Spring che questa è una classe di servizio
// Spring la registra automaticamente come bean
public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    private OperatoreRepository operatoreRepository;

    // l'interface Obbliga a implementare il metodo loadUserByUsername()
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Operatore> operatore = operatoreRepository.findByUsername(username);

        if (operatore.isEmpty()) {
            throw new UsernameNotFoundException("Utente non trovato");
        }
        return new DatabaseUserDetails(operatore.get());
    }
}
