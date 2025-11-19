package org.example.edufy_userservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaFetchResponseDTO
{
    @JsonProperty
    public Long id;

    @JsonProperty
    public String title;
}
