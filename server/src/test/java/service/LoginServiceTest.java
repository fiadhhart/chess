package service;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import responses.AuthResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Database database = new Database();
    private UserDAO userDAO = new UserMemDAO();


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "validUsername";
        String password = "validPassword";
        String email = "validEmail";
        userDAO.createUser(username, password, email);
    }


    @Test
    void testSuccessful_200() throws UnauthorizedException, DataAccessException {
        // Given
        LoginRequest request = new LoginRequest("validUsername", "validPassword");
        LoginService loginService = new LoginService();

        // When
        AuthResponse response = loginService.login(request);

        // Then
        assertEquals("validUsername", response.getUsername());
        assertNotNull(response.getAuthToken());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessful_401() throws UnauthorizedException, DataAccessException {
        // Given
        LoginRequest request = new LoginRequest("invalidUsername", "invalidPassword");
        LoginService loginService = new LoginService();

        // When & Then
        assertThrows(UnauthorizedException.class, () -> loginService.login(request));
    }
}