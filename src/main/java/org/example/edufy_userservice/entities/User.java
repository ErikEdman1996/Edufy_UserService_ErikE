package org.example.edufy_userservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false, unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String keycloaksub;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    public User()
    {

    }

    public User(Long id, String username, String keycloaksub, LocalDateTime created_at)
    {
        this.id = id;
        this.username = username;
        this.keycloaksub = keycloaksub;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKeycloaksub() {
        return keycloaksub;
    }

    public void setKeycloaksub(String keycloaksub) {
        this.keycloaksub = keycloaksub;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
