package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreationDTO dto;

    @BeforeEach
    void setup()
    {
        dto = new UserCreationDTO("Erik", "Edman");
        user = new User(1L, "Erik", "Edman", LocalDateTime.now());
    }

    @Test
    void addUser_ShouldReturnAddedUser()
    {
        // Arrange
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User testUser = userService.addUser(dto);

        // Assert
        assertEquals("Erik", testUser.getUsername());
        assertEquals("encoded_password", testUser.getPassword());
        verify(passwordEncoder).encode("Edman");
        verify(userRepository).save(any(User.class));
    }
}