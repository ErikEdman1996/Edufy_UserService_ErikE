package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.PlayRequestDTO;
import org.example.edufy_userservice.entities.Play;
import org.springframework.security.oauth2.jwt.Jwt;

public interface PlayServiceInterface
{
    Play addPlay(PlayRequestDTO dto, Jwt jwt);
}
