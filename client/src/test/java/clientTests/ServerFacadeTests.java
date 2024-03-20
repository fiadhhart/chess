package clientTests;

import facade.ServerFacade;
import org.junit.jupiter.api.*;
import requests.RegisterRequest;
import responses.AuthResponse;
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
        RegisterRequest request = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse response = facade.registerUser(request);

        assertEquals("username", response.getUsername());
        assertNotNull(response.getAuthToken());
        assertNull(response.getMessage());
    }
    @Test
    void registerUser_Negative() throws IOException {
        RegisterRequest request = new RegisterRequest("username", "password", "email@example.com");
        AuthResponse response = facade.registerUser(request);
        AuthResponse responseDuplicate = facade.registerUser(request);

        assertNull((responseDuplicate.getUsername()));
        assertNull((responseDuplicate.getAuthToken()));
        assertNotNull(responseDuplicate.getMessage());
    }

    @Test
    void loginUser_Positive() throws IOException {

    }
    @Test
    void loginUser_Negative() throws IOException {

    }

    @Test
    void logoutUser_Positive() throws IOException {

    }
    @Test
    void logoutUser_Negative() throws IOException {

    }

    @Test
    void createGame_Positive() throws IOException {

    }
    @Test
    void createGame_Negative() throws IOException {

    }

    @Test
    void joinGame_Positive() throws IOException {

    }
    @Test
    void joinGame_Negative() throws IOException {

    }

    @Test
    void listGames_Positive() throws IOException {

    }
    @Test
    void listGames_Negative() throws IOException {

    }

    @Test
    void clearDatabase_Positive() throws IOException {

    }







    }
