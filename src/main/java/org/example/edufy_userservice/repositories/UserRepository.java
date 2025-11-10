package org.example.edufy_userservice.repositories;

import org.example.edufy_userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);
    User findByKeycloaksub(String sub);
}
