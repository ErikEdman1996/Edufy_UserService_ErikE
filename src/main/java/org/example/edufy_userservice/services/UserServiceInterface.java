package org.example.edufy_userservice.services;
import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.entities.User;

public interface UserServiceInterface
{
    User getUser(Long id);
    User addUser(UserCreationDTO dto);
}
