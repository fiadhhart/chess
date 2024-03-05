package service;

import dataAccess.*;
import dataAccess.UserMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import responses.AuthResponse;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO(database);
    private UserDAO userDAO = new UserMemDAO(database);
    private GameDAO gameDAO = new GameMemDAO(database);


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
        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

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
        LoginService loginService = new LoginService(userDAO, gameDAO, authDAO);

        // When & Then
        assertThrows(UnauthorizedException.class, () -> loginService.login(request));
    }
}