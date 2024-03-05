package service;

import dataAccess.*;
import dataAccess.AuthMemDAO;
import dataAccess.GameMemDAO;
import dataAccess.UserMemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.BaseRequest;
import responses.BaseResponse;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private Database database = new Database();
    private UserDAO userDAO = new UserMemDAO(database);
    private GameDAO gameDAO = new GameMemDAO(database);
    private AuthDAO authDAO = new AuthMemDAO(database);


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username1 = "validUsername1";
        String password1 = "validPassword1";
        String email1 = "validEmail1";
        userDAO.createUser(username1, password1, email1);

        String username2 = "validUsername2";
        String password2 = "validPassword2";
        String email2 = "validEmail2";
        userDAO.createUser(username2, password2, email2);

        String gameName = "validGameName";
        gameDAO.createGame(gameName);

        authDAO.createAuth(username1);
    }


    @Test
    void testSuccessful_200() throws DataAccessException{
        //confirm setup
        assertNotEquals(0, database.getUsersSize());
        assertNotEquals(0, database.getGamesSize());
        assertNotEquals(0, database.getAuthsSize());

        //Given
        BaseRequest request = new BaseRequest();
        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);

        //When
        BaseResponse response = clearService.clear(request);

        //Then
        assertEquals(0, database.getUsersSize());
        assertEquals(0, database.getGamesSize());
        assertEquals(0, database.getAuthsSize());
        assertNull(response.getMessage());
    }
}