package org.example.edufy_userservice.configs;

import org.example.edufy_userservice.utils.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
    private final JwtAuthConverter jwtAuthConverter;

    @Autowired
    public SecurityConfig(final JwtAuthConverter jwtAuthConverter)
    {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/edufy/v1/users").permitAll()
                        .requestMatchers("/edufy/v1/users/test").authenticated()
                        .requestMatchers(HttpMethod.GET, "/edufy/v1/users/{id}").hasRole("admin")
                        .requestMatchers(HttpMethod.PUT, "/edufy/v1/users/{id}").hasRole("user")
                        .requestMatchers(HttpMethod.DELETE, "/edufy/v1/users/{id}").hasRole("admin")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2->oauth2.
                        jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }
}
