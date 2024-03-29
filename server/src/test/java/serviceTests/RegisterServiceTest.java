package serviceTests;

import dataAccess.*;
import dataAccess.memory.Database;
import dataAccess.memory.UserMemDAO;
import dataAccess.memory.AuthMemDAO;
import dataAccess.memory.GameMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.AuthResponse;
import service.RegisterService;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO(database);
    private UserDAO userDAO = new UserMemDAO(database);
    private GameDAO gameDAO = new GameMemDAO(database);


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "presentUsername";
        String password = "presentPassword";
        String email = "presentEmail";
        userDAO.createUser(username, password, email);
    }


    @Test
    void testSuccessful_200() throws BadRequestException, AlreadyTakenException, DataAccessException {
        // Given
        RegisterRequest request = new RegisterRequest("validUsername", "validPassword", "validEmail");
        RegisterService registerService = new RegisterService(userDAO, gameDAO, authDAO);

        // When
        AuthResponse response = registerService.register(request);

        // Then
        assertEquals("validUsername", response.getUsername());
        assertNotNull(response.getAuthToken());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessful_400() throws BadRequestException, AlreadyTakenException, DataAccessException {
        // Given
        RegisterRequest request = new RegisterRequest();
        RegisterService registerService = new RegisterService(userDAO, gameDAO, authDAO);

        // When & Then
        assertThrows(BadRequestException.class, () -> registerService.register(request));
    }

    @Test
    void testUnsuccessful_403() throws BadRequestException, AlreadyTakenException, DataAccessException {
        // Given
        RegisterRequest request = new RegisterRequest("presentUsername", "presentPassword", "presentEmail");
        RegisterService registerService = new RegisterService(userDAO, gameDAO, authDAO);

        // When & Then
        assertThrows(AlreadyTakenException.class, () -> registerService.register(request));
    }
}