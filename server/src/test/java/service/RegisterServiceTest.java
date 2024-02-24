package service;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.AuthResponse;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    private Database database = new Database();
    private UserDAO userDAO = new UserMemDAO();


    @BeforeEach
    void setUp() throws DataAccessException {
        database.clearDatabase();

        String username = "presentUsername";
        String password = "presentPassword";
        String email = "presentEmail";
        userDAO.createUser(username, password, email);
    }

    @Test
    void testSuccessfulRegister_200() throws BadRequestException, AlreadyTakenException, DataAccessException {
        // Given
        RegisterRequest request = new RegisterRequest("validUsername", "validPassword", "validEmail");
        RegisterService registerService = new RegisterService();

        // When
        AuthResponse response = registerService.register(request);

        // Then
        assertEquals("validUsername", response.getUsername());
        assertNotNull(response.getAuthToken());
        assertNull(response.getMessage());
    }

    @Test
    void testUnsuccessfulRegister_400() throws BadRequestException, AlreadyTakenException, DataAccessException {
        // Given
        RegisterRequest request = new RegisterRequest("", "invalidPassword", "invalidEmail");
        RegisterService registerService = new RegisterService();

        // When & Then
        assertThrows(BadRequestException.class, () -> registerService.register(request));
    }

    @Test
    void testUnsuccessfulRegister_403() throws BadRequestException, AlreadyTakenException, DataAccessException {
        // Given
        RegisterRequest request = new RegisterRequest("presentUsername", "presentPassword", "presentEmail");
        RegisterService registerService = new RegisterService();

        // When & Then
        assertThrows(AlreadyTakenException.class, () -> registerService.register(request));
    }
}