package dataAccess;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
    private static Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
        database.clearDatabase();
    }

    @Test
    public void testCreateAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.createAuth(usernameOne);

        assertNotNull(testAuthTokenOne);
        assertEquals(usernameOne, AuthDAO.getUsername(testAuthTokenOne));

    }

    @Test
    public void testGetAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.createAuth(usernameOne);
        String test = AuthDAO.getAuth(testAuthTokenOne);

        assertNotNull(AuthDAO.getAuth(testAuthTokenOne));
        assertEquals(usernameOne, AuthDAO.getUsername(testAuthTokenOne));
    }

    @Test
    public void testGetUsername() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.createAuth(usernameOne);
        String test = AuthDAO.getUsername(testAuthTokenOne);

        assertEquals(usernameOne, AuthDAO.getUsername(testAuthTokenOne));

    }

    @Test
    public void testDeleteAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.createAuth(usernameOne);

        String usernameTwo = "yourUsername";
        String testAuthTokenTwo = AuthDAO.createAuth(usernameTwo);

        assertNotNull(AuthDAO.getAuth(testAuthTokenOne));

        AuthDAO.deleteAuth(testAuthTokenOne);

        assertNull(AuthDAO.getAuth(testAuthTokenOne));

    }

    @Test
    public void testClear() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.createAuth(usernameOne);

        String usernameTwo = "yourUsername";
        String testAuthTokenTwo = AuthDAO.createAuth(usernameTwo);

        AuthDAO.clear();

        assertNull(AuthDAO.getAuth(testAuthTokenOne));
        assertNull(AuthDAO.getAuth(testAuthTokenTwo));

    }

}