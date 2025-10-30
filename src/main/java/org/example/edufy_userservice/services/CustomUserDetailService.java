package org.example.edufy_userservice.services;

import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailService implements UserDetailsService
{
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username);

        if(user == null)
        {
            throw new UsernameNotFoundException("User not found: "+ username);
        }

        System.out.println("Found user: " + user.getUsername() + ", password=" + user.getPassword());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
