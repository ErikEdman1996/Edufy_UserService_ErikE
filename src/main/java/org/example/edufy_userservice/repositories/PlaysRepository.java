package org.example.edufy_userservice.repositories;

import org.example.edufy_userservice.entities.Play;
import org.example.edufy_userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaysRepository extends JpaRepository<Play, Long>
{
    Optional<Play> findByUserAndMediaId(User user, Long mediaId);
    List<Play> findAllByUser(User user);
}
