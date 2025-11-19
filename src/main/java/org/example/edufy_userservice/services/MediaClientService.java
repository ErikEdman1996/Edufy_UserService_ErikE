package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.MediaFetchResponseDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class MediaClientService
{
    private RestClient restClient;

    public MediaClientService(RestClient.Builder restClientBuilder)
    {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:6666/edufy/v1/media")
                .build();
    }

    boolean checkIfMediaExist(Long mediaId, Jwt jwt)
    {
        try
        {
            MediaFetchResponseDTO response = restClient.get()
                    .uri("/" + mediaId)
                    .header("Authorization", "Bearer " + jwt.getTokenValue())
                    .retrieve()
                    .body(MediaFetchResponseDTO.class);

            return response != null;
        }

        catch (HttpClientErrorException.NotFound e)
        {
            return false;
        }

        catch (HttpClientErrorException.Unauthorized e)
        {
            throw new RuntimeException("Unauthorized when calling MediaService", e);
        }
    }
}
