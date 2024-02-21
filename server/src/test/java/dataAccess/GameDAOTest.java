package dataAccess;

import chess.ChessGame;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    private static Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
        database.clearDatabase();
    }

    @Test
    public void testInsertGame() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = GameDAO.insertGame(gameName);

        assertNotNull(gameID);

    }

    @Test
    public void testUpdateGameUsername() throws DataAccessException {

        Integer gameID = GameDAO.insertGame("TestGame");

        String whiteUsername = "Alice";
        String blackUsername = "Bob";

        GameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameID, whiteUsername);
        GameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameID, blackUsername);

        assertEquals(whiteUsername, GameDAO.selectAllGames().get(0).get(1));
        assertEquals(blackUsername, GameDAO.selectAllGames().get(0).get(2));

    }

    @Test
    public void testSelectAllGames() throws DataAccessException {

        GameDAO.insertGame("Game1");
        GameDAO.insertGame("Game2");

        List<List<String>> allGames = GameDAO.selectAllGames();

        assertEquals(2, allGames.size());

    }

    @Test
    public void testSelectGame_gameName() throws DataAccessException {
        String gameName = "TestGame";
        Integer gameID = GameDAO.insertGame(gameName);

        assertEquals(gameName, GameDAO.selectGame(gameName));
    }

    @Test
    public void testSelectGame_gameID() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = GameDAO.insertGame(gameName);

        assertEquals(gameID, GameDAO.selectGame(gameID));

    }

    @Test
    public void testClearGames() throws DataAccessException {

        GameDAO.insertGame("Game1");
        GameDAO.insertGame("Game2");

        GameDAO.clearGames();

        assertEquals(0, GameDAO.selectAllGames().size());

    }

}