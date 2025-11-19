package org.example.edufy_userservice.controllers;

import org.example.edufy_userservice.dtos.PlayRequestDTO;
import org.example.edufy_userservice.dtos.UserCreationDTO;
import org.example.edufy_userservice.dtos.UserUpdateDTO;
import org.example.edufy_userservice.entities.Play;
import org.example.edufy_userservice.entities.User;
import org.example.edufy_userservice.services.PlayService;
import org.example.edufy_userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;



@RestController
@RequestMapping("/edufy/v1/users")
public class UserController
{
    private final UserService userService;
    private final PlayService playService;

    @Autowired
    public UserController(final UserService userService, PlayService playService)
    {
        this.userService = userService;
        this.playService = playService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserCreationDTO userCreationDTO)
    {
        User addedUser = userService.addUser(userCreationDTO);

        return ResponseEntity.ok(addedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id)
    {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userUpdateDTO, Principal principal)
    {
        String username = principal.getName();
        User user = userService.updateUser(id, userUpdateDTO, username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/plays")
    public ResponseEntity<Play> addPlay(@RequestBody PlayRequestDTO playRequestDTO,  @AuthenticationPrincipal Jwt jwt)
    {
        Play play = playService.addPlay(playRequestDTO, jwt);

        return ResponseEntity.ok(play);
    }

    @GetMapping("/by-keycloak/{sub}")
    public ResponseEntity<User> getUserByKeycloakSub(@PathVariable String sub)
    {
        User user = userService.getUserbyKeycloaksub(sub);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint(Authentication authentication)
    {
        System.out.println("==== DEBUG AUTH ====");
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("Name: " + authentication.getName());
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("====================");
        return ResponseEntity.ok("You reached the test.");
    }
}
