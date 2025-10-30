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

    @Column(nullable = false)
    private String password;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    public User()
    {

    }

    public User(Long id, String username, String password, LocalDateTime created_at)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.created_at = created_at;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public LocalDateTime getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at)
    {
        this.created_at = created_at;
    }
}
