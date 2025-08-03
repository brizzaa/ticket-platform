
package com.wdpt6.ticket_platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

        // dep injection di databaseuserservice
        @Autowired
        private DatabaseUserDetailsService databaseUserService;

        /* filter chain => autorizzo le persone in base al ruolo */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/login", "/css/**", "/js/**")
                                                .permitAll()

                                                // Admin === accesso completo a tutti i ticket e dashboard
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .requestMatchers("/tickets/create", "/tickets/*/edit",
                                                                "/tickets/*/delete")
                                                .hasAuthority("ADMIN")
                                                // Operatore e Admin === profilo personale
                                                .requestMatchers("/operatore/**").hasAnyAuthority("ADMIN", "OPERATORE")
                                                .requestMatchers("/tickets/assigned").hasAuthority("OPERATORE")
                                                .requestMatchers("/tickets/*/view")
                                                .hasAnyAuthority("ADMIN", "OPERATORE")
                                                .requestMatchers("/tickets/*/update-status")
                                                .hasAnyAuthority("ADMIN", "OPERATORE")
                                                .requestMatchers("/tickets/*/add-note")
                                                .hasAnyAuthority("ADMIN", "OPERATORE")
                                                .requestMatchers("/profile").hasAnyAuthority("ADMIN", "OPERATORE")
                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())

                                .userDetailsService(databaseUserService);

                return http.build();
        }

        @SuppressWarnings("deprecation")
        DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(databaseUserService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        @SuppressWarnings("deprecation")
        public PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }
}
