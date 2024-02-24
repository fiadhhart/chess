package service;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.BaseRequest;
import requests.LoginRequest;
import responses.AuthResponse;
import responses.BaseResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTest {
    private Database database = new Database();
    private UserDAO userDAO = new UserMemDAO();
    private AuthDAO authDAO = new AuthMemDAO();
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
    void testSuccessfulLogout_200() throws UnauthorizedException, DataAccessException {
        assertNotEquals(0, database.getAuthsSize());

        // Given
        BaseRequest request = new BaseRequest();
        LogoutService logoutService = new LogoutService();

        // When
        BaseResponse response = logoutService.logout(request, this.authToken);

        // Then
        assertEquals(0, database.getAuthsSize());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessfulLogout_401() throws UnauthorizedException, DataAccessException {
        // Given
        BaseRequest request = new BaseRequest();
        LogoutService logoutService = new LogoutService();

        // When & Then
        assertThrows(UnauthorizedException.class, () -> logoutService.logout(request, "invalidAuthToken"));
    }
}