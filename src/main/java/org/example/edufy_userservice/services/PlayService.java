package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.PlayRequestDTO;
import org.example.edufy_userservice.entities.Play;
import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.exceptions.ResourceNotFoundException;
import org.example.edufy_userservice.repositories.PlaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class PlayService implements PlayServiceInterface {
    private final PlaysRepository playsRepository;
    private final UserService userService;
    private final MediaClientService mediaClientService;
    private static final Logger playLogger = LogManager.getLogger("PlayLogger");

    @Autowired
    public PlayService(final PlaysRepository playsRepository, final UserService userService,
            MediaClientService mediaClientService) {
        this.playsRepository = playsRepository;
        this.userService = userService;
        this.mediaClientService = mediaClientService;
    }

    @Override
    public Play addPlay(PlayRequestDTO dto, Jwt jwt) {
        String sub = jwt.getClaim("sub");
        playLogger.info("Adding play for user with sub: {} and media ID: {}", sub, dto.getMediaId());

        User user = userService.getUserbyKeycloaksub(sub);

        boolean mediaExist = mediaClientService.checkIfMediaExist(dto.getMediaId(), jwt);

        if (!mediaExist) {
            playLogger.warn("Media with ID {} does not exist", dto.getMediaId());
            throw new ResourceNotFoundException("Media", "Id", dto.getMediaId());
        }

        Optional<Play> existingPlay = playsRepository.findByUserAndMediaId(user, dto.getMediaId());

        if (existingPlay.isPresent()) {
            playLogger.info("Incrementing play count for existing play");
            Play play = existingPlay.get();
            play.incrementPlayCount();
            return playsRepository.save(play);
        }

        playLogger.info("Creating new play record");
        Play newPlay = new Play();
        newPlay.setUser(user);
        newPlay.setMediaId(dto.getMediaId());
        newPlay.setPlayCount(1);

        return playsRepository.save(newPlay);
    }

    @Override
    public List<Play> getPlays(Jwt jwt) {
        String sub = jwt.getClaim("sub");
        playLogger.info("Retrieving plays for user with sub: {}", sub);

        User user = userService.getUserbyKeycloaksub(sub);

        if (user == null) {
            playLogger.warn("User with sub {} not found", sub);
            throw new ResourceNotFoundException("User", "KeycloakSub", sub);
        }

        List<Play> plays = playsRepository.findAllByUser(user);

        return plays;
    }

    @Override
    public Play getMostPlayed(Jwt jwt) {
        String sub = jwt.getClaim("sub");
        playLogger.info("Retrieving most played media for user with sub: {}", sub);

        User user = userService.getUserbyKeycloaksub(sub);

        if (user == null) {
            playLogger.warn("User with sub {} not found", sub);
            throw new ResourceNotFoundException("User", "KeycloakSub", sub);
        }

        List<Play> plays = playsRepository.findAllByUser(user);

        if (plays.isEmpty()) {
            playLogger.info("No plays found for user");
            return null; // Or throw exception depending on requirements
        }

        Play highestPlay = plays.get(0);

        for (Play play : plays) {
            if (play.getPlayCount() > highestPlay.getPlayCount()) {
                highestPlay = play;
            }
        }

        playLogger.info("Most played media ID: {} with count: {}", highestPlay.getMediaId(),
                highestPlay.getPlayCount());

        return highestPlay;
    }
}
