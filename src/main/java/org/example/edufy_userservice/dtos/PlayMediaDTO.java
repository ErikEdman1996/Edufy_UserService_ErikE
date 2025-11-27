package org.example.edufy_userservice.dtos;

import java.util.List;

public class PlayMediaDTO {
    private Long playId;
    private Long userId;
    private Integer playCount;

    private Long mediaId;
    private String mediaTitle;

    private List<ArtistDTO> artists;
    private List<GenreDTO> genres;

    public PlayMediaDTO() {

    }

    public PlayMediaDTO(Long playId, Long userId,
            Integer playCount, Long mediaId, String mediaTitle,
            List<ArtistDTO> artists, List<GenreDTO> genres) {
        this.playId = playId;
        this.userId = userId;
        this.playCount = playCount;
        this.mediaId = mediaId;
        this.mediaTitle = mediaTitle;
        this.artists = artists;
        this.genres = genres;
    }

    public Long getPlayId() {
        return playId;
    }

    public void setPlayId(Long playId) {
        this.playId = playId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }
}
