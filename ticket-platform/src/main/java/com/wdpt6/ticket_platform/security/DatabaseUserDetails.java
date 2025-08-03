package com.wdpt6.ticket_platform.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wdpt6.ticket_platform.model.Operatore;
import com.wdpt6.ticket_platform.model.Ruolo;

// facciamo il wrapper per spring
public class DatabaseUserDetails implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(Operatore operatore) {
        this.id = operatore.getId();
        this.username = operatore.getUsername();
        this.password = operatore.getPassword();

        this.authorities = new HashSet<>();

        if (operatore.getRoles() != null) {
            for (Ruolo ruolo : operatore.getRoles()) {
                this.authorities.add(new SimpleGrantedAuthority(ruolo.getRuolo()));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return id;
    }
}