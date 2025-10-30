package org.example.edufy_userservice.configs;

import org.example.edufy_userservice.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(final CustomUserDetailService customUserDetailService)
    {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) // Inject encoder
    {
        DaoAuthenticationProvider dbProvider = new DaoAuthenticationProvider();
        dbProvider.setUserDetailsService(customUserDetailService);
        dbProvider.setPasswordEncoder(passwordEncoder);

        UserDetails admin = User.withUsername("Hugo")
                .password(passwordEncoder.encode("Ransvi"))
                .roles("ADMIN")
                .build();

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(admin);
        DaoAuthenticationProvider memoryProvider = new DaoAuthenticationProvider();
        memoryProvider.setUserDetailsService(inMemoryUserDetailsManager);
        memoryProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(memoryProvider, dbProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/edufy/v1/users").permitAll()
                        .requestMatchers("/edufy/v1/users/test").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager);

        return http.build();
    }
}
