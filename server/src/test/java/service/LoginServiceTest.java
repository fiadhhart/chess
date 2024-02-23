package service;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import responses.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private Database database = new Database();
    private UserMemDAO userDAO = new UserMemDAO();


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "validUsername";
        String password = "validPassword";
        String email = "validEmail";
        userDAO.createUser(username, password, email);
    }

    @Test
    void testSuccessfulLogin() throws UnauthorizedException, DataAccessException {
        // Given
        LoginRequest request = new LoginRequest("validUsername", "validPassword");
        LoginService loginService = new LoginService();

        // When
        LoginResponse response = loginService.login(request);

        // Then
        assertEquals("validUsername", response.getUsername());
        assertNotNull(response.getAuthToken());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessfulLogin() throws UnauthorizedException, DataAccessException {
        // Given
        LoginRequest request = new LoginRequest("invalidUsername", "invalidPassword");
        LoginService loginService = new LoginService();

        // When & Then
        assertThrows(UnauthorizedException.class, () -> loginService.login(request));
    }

    /*
    @Test
    void testLoginWithException() throws UnauthorizedException, DataAccessException {
        // Given
        LoginRequest request = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();

        // When & Then
        assertThrows(DataAccessException.class, () -> loginService.login(request));
    }
     */
}