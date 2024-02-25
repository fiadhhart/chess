package service;

import dataAccess.*;
import dataAccess.AuthMemDAO;
import dataAccess.GameMemDAO;
import dataAccess.UserMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.BaseRequest;
import responses.ListGamesResponse;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

class ListGamesServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO();
    private UserDAO userDAO = new UserMemDAO();
    private GameDAO gameDAO = new GameMemDAO();
    private String authToken;
    private int gameID1;
    private int gameID2;
    private int gameID3;

    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "validUsername";
        String password = "validPassword";
        String email = "validEmail";
        userDAO.createUser(username, password, email);

        this.authToken = authDAO.createAuth(username);

        this.gameID1 = this.gameDAO.createGame("gameName1");
        this.gameID2 = this.gameDAO.createGame("gameName2");
        this.gameID3 = this.gameDAO.createGame("gameName3");
    }

    @Test
    void testSuccessful_200() throws UnauthorizedException, DataAccessException {
        //Given
        BaseRequest request = new BaseRequest();
        ListGamesService listGamesService = new ListGamesService();

        // When
        ListGamesResponse response = listGamesService.listGames(request, this.authToken);

        // Then
        assertEquals(response.getGames().getFirst().getGameName(), gameDAO.getGame("gameName1"));
        assertEquals(response.getGames().getFirst().getGameID(), gameDAO.getGame(gameID1));
        assertEquals(response.getGames().getLast().getGameName(), gameDAO.getGame("gameName3"));
        assertEquals(response.getGames().getLast().getGameID(), gameDAO.getGame(gameID3));

        assertEquals(response.getGames().size(), database.getGamesSize());
        assertNotNull(response.getGames());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessful_401() throws UnauthorizedException, DataAccessException {
        // Given
        BaseRequest request = new BaseRequest();
        ListGamesService listGamesService = new ListGamesService();

        // When & Then
        assertThrows(UnauthorizedException.class, () -> listGamesService.listGames(request, "invalidAuthToken"));
    }

}