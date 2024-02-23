package dataAccess;

import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    private Database database;
    private GameDAO gameDAO;


    @BeforeEach
    void setUp() {
        database = new Database();
        database.clearDatabase();
        gameDAO = new GameMemDAO();
    }

    @Test
    public void testCreateGame() throws DataAccessException {

        String gameName = "TestGame";
        Integer gameID = gameDAO.createGame(gameName);

        assertNotNull(gameID);

    }

    @Test
    public void testUpdateGameUsername() throws DataAccessException {

        Integer gameID = gameDAO.createGame("TestGame");

        String whiteUsername = "Alice";
        String blackUsername = "Bob";

        gameDAO.updateGameUsername(ChessGame.TeamColor.WHITE, gameID, whiteUsername);
        gameDAO.updateGameUsername(ChessGame.TeamColor.BLACK, gameID, blackUsername);

        assertEquals(whiteUsername, gameDAO.listGames().get(0).get(1));
        assertEquals(blackUsername, gameDAO.listGames().get(0).get(2));

    }

    @Test
    public void testListGames() throws DataAccessException {

        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");

        List<List<String>> allGames = gameDAO.listGames();

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