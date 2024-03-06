package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.GameResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private String username = "testUser";


    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();

        userDAO.createUser(username, "testPassword", "test@example.com");
    }

    @AfterAll
    public static void cleanUp() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    void testCreateGame_Positive() throws DataAccessException {
        String gameName = "Test Game";
        Integer gameId = gameDAO.createGame(gameName);
        assertNotNull(gameId);

        String gameName2 = "Test Game2";
        Integer gameId2 = gameDAO.createGame(gameName2);
        assertNotNull(gameId2);
    }

    @Test
    void testCreateGame_Negative() {
        //null name
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void testIsPlayerOccupied_Positive() throws DataAccessException {
        String gameName = "Test Game";
        Integer gameId = gameDAO.createGame(gameName);

        assertFalse(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));

        gameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameId, username);

        assertTrue(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));
    }

    @Test
    void testIsPlayerOccupied_Negative() throws DataAccessException {
        String gameName = "Test Game";
        Integer gameId = gameDAO.createGame(gameName);

        assertFalse(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));

        gameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameId, username);

        assertFalse(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));
    }

    @Test
    void testUpdateGameUsername_Positive() throws DataAccessException{
        String gameName = "Test Game";
        Integer gameId = gameDAO.createGame(gameName);

        gameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameId, username);

        assertTrue(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));

        gameDAO.updateGameUsername(null, gameId, username);
    }

    @Test
    void testUpdateGameUsername_Negative() throws DataAccessException{
        String gameName = "Test Game";
        Integer gameId = gameDAO.createGame(gameName);

        assertFalse(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));

        gameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameId, username);

        String username2 = "testUser2";
        userDAO.createUser(username2, "testPassword", "test@example.com");
        gameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameId, username2);

        assertFalse(gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameId));
    }

    @Test
    void testListGames_Positive() throws DataAccessException {
        gameDAO.createGame("Game 1");
        gameDAO.createGame("Game 2");
        gameDAO.createGame("Game 3");

        List<GameResponse> games = gameDAO.listGames();

        assertNotNull(games);
        assertEquals(3, games.size());
    }

    @Test
    void testListGames_Negative() throws DataAccessException {
        gameDAO.clear();

        List<GameResponse> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    void testGetGame_gameName_Positive() throws DataAccessException {
        String gameName = "Test Game";
        gameDAO.createGame(gameName);
        assertNotNull(gameDAO.getGame(gameName));
        assertEquals(gameName, gameDAO.getGame(gameName));
    }

    @Test
    void testGetGame_gameName_Negative() throws DataAccessException {
        //game DNE
        assertNull(gameDAO.getGame("Nonexistent Game"));
    }

    @Test
    void testGetGame_gameID_Positive() throws DataAccessException {
        String gameName = "Test Game";
        Integer gameId = gameDAO.createGame(gameName);

        assertNotNull(gameDAO.getGame(gameId));
        assertEquals(gameId, gameDAO.getGame(gameId));
    }

    @Test
    void testGetGame_gameID_Negative() throws DataAccessException {
        // game DNE
        assertNull(gameDAO.getGame(-1));
    }

    @Test
    void testClear_Positive() throws DataAccessException {
        gameDAO.createGame("Game 1");
        gameDAO.createGame("Game 2");
        gameDAO.createGame("Game 3");

        gameDAO.clear();

        List<GameResponse> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }
}