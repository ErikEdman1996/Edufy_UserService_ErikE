package org.example.edufy_userservice.services;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class KeycloakService {
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.client-uuid}")
    private String clientUuid;

    @Value("${keycloak.default-role-id}")
    private String defaultRoleId;

    @Value("${keycloak.default-role-name}")
    private String defaultRoleName;

    private static final Logger keycloakLogger = LogManager.getLogger("KeycloakLogger");

    private Keycloak getKeycloakInstance() {
        keycloakLogger.info("Keycloak URL: {}", serverUrl);

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm) // login to master realm for admin
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    public String createUser(String username, String firstName, String lastName, String email, String password) {
        keycloakLogger.info("Creating user in Keycloak: {}", username);
        Keycloak keycloak = getKeycloakInstance();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            String responseBody = response.readEntity(String.class);
            keycloakLogger.error("Failed to create user. Status: {}, Body: {}", response.getStatus(), responseBody);
            throw new RuntimeException("Failed to create user: " + response.getStatus() + " " + responseBody);
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        assignRoleToUser(keycloak, userId);
        return userId;
    }

    private void assignRoleToUser(Keycloak keycloak, String userId) {
        var roleRep = new org.keycloak.representations.idm.RoleRepresentation(defaultRoleName, null, false);
        roleRep.setId(defaultRoleId);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .clientLevel(clientUuid)
                .add(List.of(roleRep));
    }

    public void updateUsername(String keycloakUserId, String newUsername) {
        Keycloak keycloak = getKeycloakInstance();

        keycloakLogger.info("Updating username for user {} to {}", keycloakUserId, newUsername);

        UserRepresentation user = keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .toRepresentation();

        user.setUsername(newUsername);

        keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .update(user);
    }

    public void deleteUser(String keycloakUserId) {
        keycloakLogger.info("Deleting user from Keycloak: {}", keycloakUserId);
        Keycloak keycloak = getKeycloakInstance();

        keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .remove();
    }

}
