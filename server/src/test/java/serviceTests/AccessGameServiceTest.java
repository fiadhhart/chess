package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.memory.AuthMemDAO;
import dataAccess.memory.Database;
import dataAccess.memory.GameMemDAO;
import dataAccess.memory.UserMemDAO;
import org.junit.jupiter.api.Test;
import requests.AccessGameRequest;
import responses.AccessGameResponse;
import service.AccessGameService;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

class AccessGameServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO(database);
    private UserDAO userDAO = new UserMemDAO(database);
    private GameDAO gameDAO = new GameMemDAO(database);
    private String authToken;

    @Test
    public void test() throws DataAccessException, UnauthorizedException, BadRequestException {
        database.clearDatabase();

        String username = "validUsername";
        String password = "validPassword";
        String email = "validEmail";
        userDAO.createUser(username, password, email);

        this.authToken = authDAO.createAuth(username);

        Integer gameID = this.gameDAO.createGame("gameName1");

        AccessGameRequest request = new AccessGameRequest(gameID);
        AccessGameService accessGameService = new AccessGameService(userDAO, gameDAO, authDAO);

        // When
        AccessGameResponse response = accessGameService.accessGame(request, this.authToken);

        assertNull(response.getMessage());
    }


}