package org.example.edufy_userservice.controllers;

import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/edufy/v1/users")
public class UserController
{
    final UserService userService;

    @Autowired
    public UserController(final UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserCreationDTO userCreationDTO)
    {
        User addedUser = userService.addUser(userCreationDTO);

        return ResponseEntity.ok(addedUser);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint(Authentication authentication)
    {
        System.out.println(authentication.getName() + " " + authentication.getAuthorities() + " " + authentication.getPrincipal() + " " + authentication.getCredentials());

        return ResponseEntity.ok("You reached the test.");
    }
}
