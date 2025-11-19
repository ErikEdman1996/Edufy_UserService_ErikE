package org.example.edufy_userservice.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "plays",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "media_id"})
)
public class Play
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "media_id", nullable = false)
    private Long mediaId;

    @Column(name = "play_count")
    private Integer playCount;

    public Play()
    {
    }

    public Play(User user, Long mediaId)
    {
        this.user = user;
        this.mediaId = mediaId;
        this.playCount = 1;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Long getMediaId()
    {
        return mediaId;
    }

    public void setMediaId(Long mediaId)
    {
        this.mediaId = mediaId;
    }

    public Integer getPlayCount()
    {
        return playCount;
    }

    public void setPlayCount(Integer playCount)
    {
        this.playCount = playCount;
    }

    public void incrementPlayCount()
    {
        this.playCount++;
    }
}
