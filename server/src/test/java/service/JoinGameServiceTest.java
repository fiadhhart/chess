package service;

import chess.ChessGame;
import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.JoinGameRequest;
import responses.BaseResponse;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {
    private Database database = new Database();
    private AuthDAO authDAO = new AuthMemDAO();
    private UserDAO userDAO = new UserMemDAO();
    private GameDAO gameDAO = new GameMemDAO();
    private String authToken1;
    private String authToken2;
    private String authToken3;
    private int gameID;


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username1 = "validUsername1";
        String password1 = "validPassword1";
        String email1 = "validEmail1";
        userDAO.createUser(username1, password1, email1);
        this.authToken1 = authDAO.createAuth(username1);

        String username2 = "validUsername2";
        String password2 = "validPassword2";
        String email2 = "validEmail2";
        userDAO.createUser(username2, password2, email2);
        this.authToken2 = authDAO.createAuth(username2);

        String username3 = "validUsername3";
        String password3 = "validPassword3";
        String email3 = "validEmail3";
        userDAO.createUser(username3, password3, email3);
        this.authToken3 = authDAO.createAuth(username3);

        this.gameID = this.gameDAO.createGame("gameName");
    }

    @Test
    void testSuccessful_200() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        //Given
        JoinGameRequest requestW = new JoinGameRequest(ChessGame.TeamColor.WHITE, this.gameID);
        JoinGameRequest requestB = new JoinGameRequest(ChessGame.TeamColor.BLACK, this.gameID);
        JoinGameRequest requestE = new JoinGameRequest(this.gameID);
        JoinGameService joinGameService = new JoinGameService();

        // When
        BaseResponse response1 = joinGameService.joinGame(requestW, this.authToken1);
        BaseResponse response2 = joinGameService.joinGame(requestB, this.authToken2);
        BaseResponse response3 = joinGameService.joinGame(requestE, this.authToken3);

        // Then
        assertNull(response1.getMessage());
        assertNull(response2.getMessage());
        assertNull(response3.getMessage());
    }

    @Test
    void testUnsuccessful_400() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        // Given
        int invalidGameID = 5;
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, invalidGameID);
        JoinGameService joinGameService = new JoinGameService();

        // When & Then
        assertThrows(BadRequestException.class, () -> joinGameService.joinGame(request, this.authToken1));
    }

    @Test
    void testUnsuccessful_401() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        // Given
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, this.gameID);
        JoinGameService joinGameService = new JoinGameService();

        // When & Then
        assertThrows(UnauthorizedException.class, () -> joinGameService.joinGame(request, "invalidAuthToken"));
    }

    @Test
    void testUnsuccessful_403() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        //Given
        JoinGameRequest requestW = new JoinGameRequest(ChessGame.TeamColor.WHITE, this.gameID);
        JoinGameRequest requestW2 = new JoinGameRequest(ChessGame.TeamColor.WHITE, this.gameID);
        JoinGameService joinGameService = new JoinGameService();
        BaseResponse response = joinGameService.joinGame(requestW, this.authToken1);

        // When & Then
        assertThrows(AlreadyTakenException.class, () -> joinGameService.joinGame(requestW2, this.authToken2));
    }

}