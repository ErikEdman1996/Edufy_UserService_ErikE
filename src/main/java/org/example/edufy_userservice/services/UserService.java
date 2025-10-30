package org.example.edufy_userservice.services;
import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.edufy_userservice.entities.User;

import java.time.LocalDateTime;

@Service
public class UserService implements UserServiceInterface
{
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(Long id)
    {
        return null;
    }

    @Override
    public User addUser(UserCreationDTO dto)
    {
        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreated_at(LocalDateTime.now());

        return userRepository.save(user);
    }
}
