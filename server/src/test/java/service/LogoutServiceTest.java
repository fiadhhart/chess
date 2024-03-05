package service;

import dataAccess.*;
import dataAccess.AuthMemDAO;
import dataAccess.UserMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.BaseRequest;
import responses.BaseResponse;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO(database);
    private UserDAO userDAO = new UserMemDAO(database);
    private GameDAO gameDAO = new GameMemDAO(database);
    private String authToken;


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "validUsername";
        String password = "validPassword";
        String email = "validEmail";
        userDAO.createUser(username, password, email);

        this.authToken = authDAO.createAuth(username);
    }


    @Test
    void testSuccessful_200() throws UnauthorizedException, DataAccessException {
        assertNotEquals(0, database.getAuthsSize());

        // Given
        BaseRequest request = new BaseRequest();
        LogoutService logoutService = new LogoutService(userDAO, gameDAO, authDAO);

        // When
        BaseResponse response = logoutService.logout(request, this.authToken);

        // Then
        assertEquals(0, database.getAuthsSize());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessful_401() throws UnauthorizedException, DataAccessException {
        // Given
        BaseRequest request = new BaseRequest();
        LogoutService logoutService = new LogoutService(userDAO, gameDAO, authDAO);

        // When & Then
        assertThrows(UnauthorizedException.class, () -> logoutService.logout(request, "invalidAuthToken"));
    }
}