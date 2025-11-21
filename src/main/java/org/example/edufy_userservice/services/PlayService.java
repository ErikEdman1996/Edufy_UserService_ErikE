package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.PlayRequestDTO;
import org.example.edufy_userservice.entities.Play;
import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.exceptions.ResourceNotFoundException;
import org.example.edufy_userservice.repositories.PlaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayService implements PlayServiceInterface
{
    private final PlaysRepository playsRepository;
    private final UserService userService;
    private final MediaClientService mediaClientService;

    @Autowired
    public PlayService(final PlaysRepository playsRepository, final UserService userService, MediaClientService mediaClientService)
    {
        this.playsRepository = playsRepository;
        this.userService = userService;
        this.mediaClientService = mediaClientService;
    }

    @Override
    public Play addPlay(PlayRequestDTO dto, Jwt jwt)
    {
        String sub = jwt.getClaim("sub");

        User user = userService.getUserbyKeycloaksub(sub);

        boolean mediaExist = mediaClientService.checkIfMediaExist(dto.getMediaId(), jwt);

        if(!mediaExist)
        {
            throw new ResourceNotFoundException("Media", "Id", dto.getMediaId());
        }

        Optional<Play> existingPlay = playsRepository.findByUserAndMediaId(user, dto.getMediaId());

        if(existingPlay.isPresent())
        {
            Play play = existingPlay.get();
            play.incrementPlayCount();
            return playsRepository.save(play);
        }

        Play newPlay = new Play();
        newPlay.setUser(user);
        newPlay.setMediaId(dto.getMediaId());
        newPlay.setPlayCount(1);

        return playsRepository.save(newPlay);
    }

    @Override
    public List<Play> getPlays(Jwt jwt)
    {
        String sub = jwt.getClaim("sub");

        User user = userService.getUserbyKeycloaksub(sub);

        if(user == null)
        {
            throw new ResourceNotFoundException("User", "KeycloakSub", sub);
        }

        List<Play> plays = playsRepository.findAllByUser(user);

        return plays;
    }

    @Override
    public Play getMostPlayed(Jwt jwt)
    {
        String sub = jwt.getClaim("sub");

        User user = userService.getUserbyKeycloaksub(sub);

        if(user == null)
        {
            throw new ResourceNotFoundException("User", "KeycloakSub", sub);
        }

        List<Play> plays = playsRepository.findAllByUser(user);

        Play highestPlay = plays.get(0);

        for(Play play : plays)
        {
             if(play.getPlayCount() > highestPlay.getPlayCount())
             {
                 highestPlay = play;
             }
        }

        return highestPlay;
    }
}
