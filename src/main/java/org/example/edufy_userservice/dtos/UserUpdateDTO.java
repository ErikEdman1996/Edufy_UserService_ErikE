package org.example.edufy_userservice.dtos;

public class UserUpdateDTO
{
    private String username;

    public UserUpdateDTO()
    {

    }

    public UserUpdateDTO(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
