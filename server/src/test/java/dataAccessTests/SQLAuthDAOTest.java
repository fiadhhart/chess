package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {
    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private String username = "testUser";

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();

        userDAO.createUser(username, "testPassword", "test@example.com");
    }

    @AfterAll
    public static void cleanUp() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    void testCreateAuth_Positive() throws DataAccessException {
        String authToken = authDAO.createAuth(username);

        assertNotNull(authToken);
        assertEquals(username, authDAO.getUsername(authToken));
    }

    @Test
    void testCreateAuth_Negative() throws DataAccessException {
        String wrongUsername = "wrongUser";

        //associated user DNE
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(wrongUsername));
    }

    @Test
    void testGetAuth_Positive() throws DataAccessException {
        String authToken = authDAO.createAuth(username);

        assertEquals(authToken, authDAO.getAuth(authToken));
    }

    @Test
    void testGetAuth_Negative() throws DataAccessException {
        String wrongAuthToken = "nonExistingToken";

        //auth DNE
        assertNull(authDAO.getAuth(wrongAuthToken));
    }

    @Test
    void testGetUsername_Positive() throws DataAccessException {
        String authToken = authDAO.createAuth(username);

        assertEquals(username, authDAO.getUsername(authToken));
    }

    @Test
    void testGetUsername_Negative() throws DataAccessException {
        String wrongAuthToken = "nonExistingToken";

        //auth DNE
        assertNull(authDAO.getUsername(wrongAuthToken));
    }

    @Test
    void testDeleteAuth_Positive() throws DataAccessException {
        String authToken = authDAO.createAuth(username);
        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getUsername(authToken));
    }

    @Test
    void testDeleteAuth_Negative() {
        String wrongAuthToken = "nonExistingToken";

        //auth DNE
        assertDoesNotThrow(() -> authDAO.deleteAuth(wrongAuthToken));
    }

    @Test
    public void testClear_Positive() throws DataAccessException {
        String username2 = "testUser2";
        userDAO.createUser(username2, "testPassword", "test@example.com");

        authDAO.createAuth(username);
        authDAO.createAuth(username2);
        authDAO.createAuth(username);

        authDAO.clear();

        assertNull(authDAO.getAuth(username));
        assertNull(authDAO.getAuth(username2));
    }
}