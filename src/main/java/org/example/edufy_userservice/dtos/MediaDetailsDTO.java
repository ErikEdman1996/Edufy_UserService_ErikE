package org.example.edufy_userservice.dtos;

import java.util.List;

public class MediaDetailsDTO {
    private Long id;
    private String title;
    private Long albumId;
    private String type;
    private String url;
    private List<ArtistDTO> artists;
    private List<GenreDTO> genres;

    public MediaDetailsDTO() {
    }

    public MediaDetailsDTO(Long id, String title, Long albumId, String type, String url, List<ArtistDTO> artists,
            List<GenreDTO> genres) {
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.type = type;
        this.url = url;
        this.artists = artists;
        this.genres = genres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
