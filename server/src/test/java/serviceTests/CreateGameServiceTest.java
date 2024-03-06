package serviceTests;

import dataAccess.*;
import dataAccess.memory.AuthMemDAO;
import dataAccess.memory.Database;
import dataAccess.memory.UserMemDAO;
import dataAccess.memory.GameMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.CreateGameService;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO(database);
    private GameDAO gameDAO = new GameMemDAO(database);
    private UserDAO userDAO = new UserMemDAO(database);
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
    void testSuccessful_200() throws BadRequestException, UnauthorizedException, DataAccessException {
        assertEquals(0, database.getGamesSize());

        // Given
        CreateGameRequest request = new CreateGameRequest("validGameName");
        CreateGameService createGameService = new CreateGameService(userDAO, gameDAO, authDAO);

        // When
        CreateGameResponse response = createGameService.createGame(request, this.authToken);

        // Then
        assertNotEquals(0, database.getGamesSize());
        assertNotNull(response.getGameID());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessful_400() throws BadRequestException, UnauthorizedException, DataAccessException {
        // Given
        CreateGameRequest request = new CreateGameRequest("repeatedGameName");
        CreateGameService createGameService = new CreateGameService(userDAO, gameDAO, authDAO);

        // When & Then
        CreateGameResponse response = createGameService.createGame(request, this.authToken);
        assertThrows(BadRequestException.class, () -> createGameService.createGame(request, this.authToken));
    }

    @Test
    void testUnsuccessful_401() throws BadRequestException, UnauthorizedException, DataAccessException {
        // Given
        CreateGameRequest request = new CreateGameRequest("validGameName");
        CreateGameService createGameService = new CreateGameService(userDAO, gameDAO, authDAO);

        // When & Then
        assertThrows(UnauthorizedException.class, () -> createGameService.createGame(request, "invalidAuthToken"));
    }
}