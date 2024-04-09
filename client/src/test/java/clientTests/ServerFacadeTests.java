package clientTests;

import chess.ChessGame;
import facade.ServerFacade;
import org.junit.jupiter.api.*;
import requests.*;
import responses.AuthResponse;
import responses.BaseResponse;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    void clearDatabase() throws IOException {
        facade.clearDatabase(null);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void registerUser_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);

        assertEquals("username", registerResponse.getUsername());
        assertNotNull(registerResponse.getAuthToken());
        assertNull(registerResponse.getMessage());
    }
    @Test
    void registerUser_Negative() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        AuthResponse registerResponse_duplicate = facade.registerUser(registerRequest);  //AlreadyTaken
        //BadRequest - is currently possible to register with no info

        assertNull((registerResponse_duplicate.getUsername()));
        assertNull((registerResponse_duplicate.getAuthToken()));
        assertNotNull(registerResponse_duplicate.getMessage());
    }

    @Test
    void loginUser_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        BaseRequest logoutRequest = new BaseRequest();
        BaseResponse logoutResponse = facade.logoutUser(logoutRequest, authToken);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        AuthResponse loginResponse = facade.loginUser(loginRequest);

        assertEquals("username", loginResponse.getUsername());
        assertNotNull(loginResponse.getAuthToken());
        assertNull(loginResponse.getMessage());
    }
    @Test
    void loginUser_Negative() throws IOException {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        AuthResponse loginResponse = facade.loginUser(loginRequest);    //Unauthorized

        assertNull(loginResponse.getUsername());
        assertNull(loginResponse.getAuthToken());
        assertNotNull(loginResponse.getMessage());
    }

    @Test
    void logoutUser_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        BaseRequest logoutRequest = new BaseRequest();
        BaseResponse logoutResponse = facade.logoutUser(logoutRequest, authToken);

        assertNull(logoutResponse.getMessage());
    }
    @Test
    void logoutUser_Negative() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        BaseRequest logoutRequest = new BaseRequest();
        BaseResponse logoutResponse = facade.logoutUser(logoutRequest, "invalidAuthToken"); //Unauthorized

        assertNotNull(logoutResponse.getMessage());
    }

    @Test
    void createGame_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest, authToken);

        assertNotNull(createGameResponse.getGameID());
        assertNull(createGameResponse.getMessage());
    }
    @Test
    void createGame_Negative() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResponse createGameResponse_badAuth = facade.createGame(createGameRequest, "invalidAuthToken");   //Unauthorized
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest, authToken);
        CreateGameResponse createGameResponse_duplicate = facade.createGame(createGameRequest, authToken);   //BadRequest

        assertNull(createGameResponse_badAuth.getGameID());
        assertNotNull(createGameResponse_badAuth.getMessage());
        assertNull(createGameResponse_duplicate.getGameID());
        assertNotNull(createGameResponse_duplicate.getMessage());
    }

    @Test
    void joinGame_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest, authToken);
        Integer gameID = createGameResponse.getGameID();

        JoinGameRequest joinGameRequest_white = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);   //join as white
        BaseResponse joinGameResponse_white = facade.joinGame(joinGameRequest_white, authToken);
        JoinGameRequest joinGameRequest_black = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);   //join as black
        BaseResponse joinGameResponse_black = facade.joinGame(joinGameRequest_black, authToken);
        JoinGameRequest joinGameRequest_observer = new JoinGameRequest(gameID);   //join as observer
        BaseResponse joinGameResponse_observer = facade.joinGame(joinGameRequest_observer, authToken);

        assertNull(joinGameResponse_white.getMessage());
        assertNull(joinGameResponse_black.getMessage());
        assertNull(joinGameResponse_observer.getMessage());
    }
    @Test
    void joinGame_Negative() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest, authToken);
        Integer gameID = createGameResponse.getGameID();
        JoinGameRequest joinGameRequest_white = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);

        BaseResponse joinGameResponse_badAuth = facade.joinGame(joinGameRequest_white, "invalidAuthToken"); //Unauthorized
        BaseResponse joinGameResponse_white = facade.joinGame(joinGameRequest_white, authToken);
        BaseResponse joinGameResponse_whiteAgain = facade.joinGame(joinGameRequest_white, authToken);   //AlreadyTaken
        JoinGameRequest joinGameRequest_empty = new JoinGameRequest();
        BaseResponse joinGameResponse_empty = facade.joinGame(joinGameRequest_empty, authToken);  //BadRequest

        assertNotNull(joinGameResponse_badAuth.getMessage());
        //assertNotNull(joinGameResponse_whiteAgain.getMessage());  //this functionality does work, but I changed it so the same user can return to a game
        assertNotNull(joinGameResponse_empty.getMessage());
    }

    @Test
    void listGames_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest, authToken);
        Integer gameID = createGameResponse.getGameID();
        JoinGameRequest joinGameRequest_white = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);   //join as white
        BaseResponse joinGameResponse_white = facade.joinGame(joinGameRequest_white, authToken);
        JoinGameRequest joinGameRequest_black = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);   //join as black
        BaseResponse joinGameResponse_black = facade.joinGame(joinGameRequest_black, authToken);
        CreateGameRequest createGameRequest_2 = new CreateGameRequest("gameName2");
        CreateGameResponse createGameResponse_2 = facade.createGame(createGameRequest_2, authToken);
        Integer gameID_2 = createGameResponse_2.getGameID();
        JoinGameRequest joinGameRequest_2black = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID_2);   //join as black
        BaseResponse joinGameResponse_2black = facade.joinGame(joinGameRequest_2black, authToken);

        BaseRequest listGamesRequest = new BaseRequest();
        ListGamesResponse listGamesResponse = facade.listGames(listGamesRequest, authToken);

        assertEquals(2, listGamesResponse.getGames().size());
        assertNull(listGamesResponse.getMessage());
    }
    @Test
    void listGames_Negative() throws IOException {
        BaseRequest listGamesRequest = new BaseRequest();
        ListGamesResponse listGamesResponse = facade.listGames(listGamesRequest, "invalidAuthToken");   //Unauthorized

        assertNull(listGamesResponse.getGames());
        assertNotNull(listGamesResponse.getMessage());
    }

    @Test
    void clearDatabase_Positive() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse registerResponse = facade.registerUser(registerRequest);
        String authToken = registerResponse.getAuthToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest, authToken);

        BaseRequest clearDatabaseRequest = new BaseRequest();
        BaseResponse clearDatabaseResponse = facade.clearDatabase(clearDatabaseRequest);

        assertNull(clearDatabaseResponse.getMessage());
    }

    }