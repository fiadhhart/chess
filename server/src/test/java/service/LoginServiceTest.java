package service;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import responses.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private static Database database = new Database();;
    private static UserMemDAO userDAO = new UserMemDAO();


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "validUsername";
        String password = "validPassword";
        String email = "validEmail";
        UserMemDAO.createUser(username, password, email);
    }

    @Test
    void testSuccessfulLogin() {
        // Given
        LoginRequest request = new LoginRequest("validUsername", "validPassword");
        LoginService loginService = new LoginService();

        // When
        LoginResponse response = loginService.login(request);

        // Then
        assertEquals(200, response.getStatusCode());
        assertEquals("validUsername", response.getUsername());
        assertNotNull(response.getAuthToken());
    }

    @Test
    void testUnsuccessfulLogin() {
        // Given
        LoginRequest request = new LoginRequest("invalidUsername", "invalidPassword");
        LoginService loginService = new LoginService();

        // When
        LoginResponse response = loginService.login(request);

        // Then
        assertEquals(401, response.getStatusCode());
        assertEquals("Error: unauthorized", response.getMessage());
    }

    @Test
    void testLoginWithException() {
        // Given
        LoginRequest request = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();

        // When
        LoginResponse response = loginService.login(request);

        // Then
        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().startsWith("Error: "));
    }
}