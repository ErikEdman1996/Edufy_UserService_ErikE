package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.dtos.UserUpdateDTO;
import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.exceptions.ResourceNotFoundException;
import org.example.edufy_userservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreationDTO creationDTO;

    @BeforeEach
    void setup() {
        creationDTO = new UserCreationDTO("erik", "Erik", "Edman", "erik@edman.com", "12345");
        user = new User(1L, "erik", "keycloak-123", LocalDateTime.now());
    }

    @Test
    void addUser_ShouldReturnAddedUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.addUser(creationDTO);

        // Assert
        assertEquals("erik", savedUser.getUsername());
        assertEquals("keycloak-123", savedUser.getKeycloaksub());
        assertNotNull(savedUser.getCreated_at());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUser_ShouldReturnCorrectUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.getUser(1L);

        // Assert
        assertEquals(1L, foundUser.getId());
        assertEquals("erik", foundUser.getUsername());
        assertEquals("keycloak-123", foundUser.getKeycloaksub());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> userService.getUser(99L));

        assertTrue(ex.getMessage().contains("User"));
        verify(userRepository).findById(99L);
    }

    @Test
    void updateUser_ShouldUpdateUsername() {
        // Arrange
        UserUpdateDTO updateDTO = new UserUpdateDTO("newErik");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUser(1L, updateDTO, "erik");

        // Assert
        assertEquals("newErik", updatedUser.getUsername());
        assertEquals("keycloak-123", updatedUser.getKeycloaksub());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowResourceNotFound_WhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        UserUpdateDTO dto = new UserUpdateDTO("newName");

        ResourceNotFoundException ex =
                assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(99L, dto, "erik"));

        assertTrue(ex.getMessage().contains("User"));
        verify(userRepository).findById(99L);
    }

    @Test
    void updateUser_ShouldThrowUnauthorized_WhenDifferentUserTriesToUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserUpdateDTO dto = new UserUpdateDTO("newErik");

        assertThrows(SecurityException.class, () -> userService.updateUser(1L, dto, "anotherUser"));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }
}
