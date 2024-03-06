package dataAccessTests.memory;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.memory.Database;
import dataAccess.memory.GameMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.GameResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMemDAOTest {

    private Database database;
    private GameDAO gameDAO;


    @BeforeEach
    void setUp() {
        database = new Database();
        database.clearDatabase();
        gameDAO = new GameMemDAO(database);
    }


    @Test
    public void testCreateGame() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = gameDAO.createGame(gameName);

        assertNotNull(gameID);
    }

    @Test
    public void testIsPlayerOccupied() throws DataAccessException{
        Integer gameID = gameDAO.createGame("TestGame");

        assertEquals(false, gameDAO.isPlayerOccupied(null, gameID));

        String whiteUsername = "testWhiteUsername";
        String blackUsername = "testBlackUsername";

        assertEquals(false, gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameID));
        assertEquals(false, gameDAO.isPlayerOccupied(ChessGame.TeamColor.BLACK, gameID));

        gameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameID, whiteUsername);

        assertEquals(true, gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameID));
        assertEquals(false, gameDAO.isPlayerOccupied(ChessGame.TeamColor.BLACK, gameID));

        gameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameID, blackUsername);

        assertEquals(true, gameDAO.isPlayerOccupied(ChessGame.TeamColor.WHITE, gameID));
        assertEquals(true, gameDAO.isPlayerOccupied(ChessGame.TeamColor.BLACK, gameID));
    }

    @Test
    public void testUpdateGameUsername() throws DataAccessException {

        Integer gameID = gameDAO.createGame("TestGame");

        String whiteUsername = "testWhiteUsername";
        String blackUsername = "testBlackUsername";

        gameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameID, whiteUsername);
        gameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameID, blackUsername);

        assertEquals(whiteUsername, gameDAO.listGames().getFirst().getWhiteUsername());
        assertEquals(blackUsername, gameDAO.listGames().getFirst().getBlackUsername());
    }

    @Test
    public void testListGames() throws DataAccessException {

        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");

        List<GameResponse> allGames = gameDAO.listGames();

        assertEquals(2, allGames.size());
    }

    @Test
    public void testGetGame_gameName() throws DataAccessException {
        String gameName = "TestGame";
        Integer gameID = gameDAO.createGame(gameName);

        assertEquals(gameName, gameDAO.getGame(gameName));
    }

    @Test
    public void testGetGame_gameID() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = gameDAO.createGame(gameName);

        assertEquals(gameID, gameDAO.getGame(gameID));
    }

    @Test
    public void testClear() throws DataAccessException {

        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");

        gameDAO.clear();

        assertEquals(0, gameDAO.listGames().size());
    }
}