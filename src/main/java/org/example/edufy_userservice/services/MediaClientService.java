package org.example.edufy_userservice.services;

import org.example.edufy_userservice.dtos.MediaFetchResponseDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class MediaClientService {
    private RestClient restClient;
    private static final Logger mediaClientLogger = LogManager.getLogger("MediaClientLogger");

    public MediaClientService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:6666/edufy/v1/media")
                .build();
    }

    boolean checkIfMediaExist(Long mediaId, Jwt jwt) {
        mediaClientLogger.info("Checking if media exists with ID: {}", mediaId);
        try {
            MediaFetchResponseDTO response = restClient.get()
                    .uri("/" + mediaId)
                    .header("Authorization", "Bearer " + jwt.getTokenValue())
                    .retrieve()
                    .body(MediaFetchResponseDTO.class);

            return response != null;
        }

        catch (HttpClientErrorException.NotFound e) {
            mediaClientLogger.warn("Media with ID {} not found", mediaId);
            return false;
        }

        catch (HttpClientErrorException.Unauthorized e) {
            mediaClientLogger.error("Unauthorized when calling MediaService", e);
            throw new RuntimeException("Unauthorized when calling MediaService", e);
        }
    }
}
