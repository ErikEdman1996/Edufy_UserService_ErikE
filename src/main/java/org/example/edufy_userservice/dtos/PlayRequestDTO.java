package org.example.edufy_userservice.dtos;

public class PlayRequestDTO
{
    private Long mediaId;

    public PlayRequestDTO()
    {

    }

    public PlayRequestDTO(Long mediaId)
    {
        this.mediaId = mediaId;
    }

    public Long getMediaId()
    {
        return mediaId;
    }

    public void setMediaId(Long mediaId)
    {
        this.mediaId = mediaId;
    }
}
