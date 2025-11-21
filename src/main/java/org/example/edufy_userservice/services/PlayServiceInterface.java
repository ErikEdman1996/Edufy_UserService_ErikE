package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.PlayRequestDTO;
import org.example.edufy_userservice.entities.Play;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface PlayServiceInterface
{
    Play addPlay(PlayRequestDTO dto, Jwt jwt);
    List<Play> getPlays(Jwt jwt);
    Play getMostPlayed(Jwt jwt);
}
