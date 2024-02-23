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
    public void testCreateGame() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = GameDAO.createGame(gameName);

        assertNotNull(gameID);

    }

    @Test
    public void testUpdateGameUsername() throws DataAccessException {

        Integer gameID = GameDAO.createGame("TestGame");

        String whiteUsername = "Alice";
        String blackUsername = "Bob";

        GameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameID, whiteUsername);
        GameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameID, blackUsername);

        assertEquals(whiteUsername, GameDAO.listGames().get(0).get(1));
        assertEquals(blackUsername, GameDAO.listGames().get(0).get(2));

    }

    @Test
    public void testListGames() throws DataAccessException {

        GameDAO.createGame("Game1");
        GameDAO.createGame("Game2");

        List<List<String>> allGames = GameDAO.listGames();

        assertEquals(2, allGames.size());

    }

    @Test
    public void testGetGame_gameName() throws DataAccessException {
        String gameName = "TestGame";
        Integer gameID = GameDAO.createGame(gameName);

        assertEquals(gameName, GameDAO.getGame(gameName));
    }

    @Test
    public void testGetGame_gameID() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = GameDAO.createGame(gameName);

        assertEquals(gameID, GameDAO.getGame(gameID));

    }

    @Test
    public void testClear() throws DataAccessException {

        GameDAO.createGame("Game1");
        GameDAO.createGame("Game2");

        GameDAO.clear();

        assertEquals(0, GameDAO.listGames().size());

    }

}