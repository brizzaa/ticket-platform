
package com.wdpt6.ticket_platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        // dep injection di databaseuserservice
        @Autowired
        private DatabaseUserDetailsService databaseUserService;

        /* filter chain => autorizzo le persone in base al ruolo */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/", "/login").permitAll()

                                                // Admin === accesso completo a tutti i ticket e dashboard
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .requestMatchers("/tickets/create", "/tickets/*/edit",
                                                                "/tickets/*/delete")
                                                .hasAuthority("ADMIN")
                                                // Operatore === solo ticket assegnati e profilo personale
                                                .requestMatchers("/operatore/**").hasAuthority("OPERATORE")
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
                                                .logoutSuccessUrl("/")
                                                .permitAll())

                                .userDetailsService(databaseUserService);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
                /*
                 * in base a quello che mettiamo nel db determina l'hshing function della
                 * password
                 */
        }
}
