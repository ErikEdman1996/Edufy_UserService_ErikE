package org.example.edufy_userservice.services;
import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.dtos.UserUpdateDTO;
import org.example.edufy_userservice.entities.User;

public interface UserServiceInterface
{
    User getUser(Long id);
    User addUser(UserCreationDTO dto);
    User getUserbyUsername(String username);
    User getUserbyKeycloaksub(String sub);
    User updateUser(long id, UserUpdateDTO dto, String username);
    void deleteUser(long id);
}
