package org.example.edufy_userservice.services;
import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.dtos.UserUpdateDTO;
import org.example.edufy_userservice.exceptions.ResourceNotFoundException;
import org.example.edufy_userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.example.edufy_userservice.entities.User;

import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class UserService implements UserServiceInterface
{
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    private static final Logger userLogger = LogManager.getLogger("UserLogger");

    @Autowired
    public UserService(final UserRepository userRepository, final KeycloakService keycloakService)
    {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }

    @Override
    public User getUser(Long id)
    {
        userLogger.info("Retrieving user with ID {}", id);
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
        {
            userLogger.warn("Could not find user with ID {}", id);
            throw new ResourceNotFoundException("User", "id", id);
        }

        return user.get();
    }

    @Override
    public User addUser(UserCreationDTO dto)
    {
        userLogger.info("Adding user to Keycloak {}", dto.getUsername());
        String keycloakId = keycloakService.createUser(
                dto.getUsername(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPassword());

        userLogger.info("Adding user to local database {}", dto.getUsername());
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setKeycloaksub(keycloakId);
        user.setCreated_at(LocalDateTime.now());

        user = userRepository.save(user);

        userLogger.info("User: {} with ID {} - successfully added.", user.getUsername(), user.getId());
        return user;
    }

    @Override
    public User getUserbyUsername(String username)
    {
        userLogger.info("Retrieving user with name {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserbyKeycloaksub(String sub)
    {
        userLogger.info("Retrieving user with keycloak sub {}", sub);
        return userRepository.findByKeycloaksub(sub);
    }

    @Override
    public User updateUser(long id, UserUpdateDTO dto, String username)
    {
        userLogger.info("Retrieving user with ID {}", id);
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
        {
            userLogger.warn("Could not find user with ID {}", id);
            throw new ResourceNotFoundException("User", "id", id);
        }

        if(!user.get().getUsername().equals(username))
        {
            userLogger.warn("User with username {} is not authorized.", username);
            throw new UsernameNotFoundException("Not authorized");
        }

        if(!user.get().getUsername().equals(dto.getUsername()) && dto.getUsername() != null)
        {
            user.get().setUsername(dto.getUsername());
        }

        userLogger.info("Updating user in Keycloak with sub {}", user.get().getKeycloaksub());
        keycloakService.updateUsername(user.get().getKeycloaksub(), dto.getUsername());

        userLogger.info("Updating user in local database {}", user.get().getId());
        userRepository.save(user.get());

        return user.get();
    }

    @Override
    public void deleteUser(long id)
    {
        userLogger.info("Retrieving user with ID {}", id);
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
        {
            userLogger.warn("Could not find user with ID {}", id);
            throw new ResourceNotFoundException("User", "id", id);
        }

        userLogger.info("Deleting user with sub {} from Keycloak", user.get().getKeycloaksub());
        keycloakService.deleteUser(user.get().getKeycloaksub());

        userLogger.info("Deleting user in local database {}", user.get().getId());
        userRepository.delete(user.get());
    }
}
