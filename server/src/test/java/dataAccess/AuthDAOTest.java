package dataAccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
    private Database database;
    private AuthDAO authDAO;


    @BeforeEach
    void setUp() {
        database = new Database();
        database.clearDatabase();
        authDAO = new AuthMemDAO();
    }


    @Test
    public void testCreateAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = authDAO.createAuth(usernameOne);

        assertNotNull(testAuthTokenOne);
        assertEquals(usernameOne, authDAO.getUsername(testAuthTokenOne));
    }

    @Test
    public void testGetAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = authDAO.createAuth(usernameOne);
        String test = authDAO.getAuth(testAuthTokenOne);

            assertNotNull(authDAO.getAuth(testAuthTokenOne));
        assertEquals(usernameOne, authDAO.getUsername(testAuthTokenOne));
    }

    @Test
    public void testGetUsername() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = authDAO.createAuth(usernameOne);
        String test = authDAO.getUsername(testAuthTokenOne);

        assertEquals(usernameOne, authDAO.getUsername(testAuthTokenOne));
    }

    @Test
    public void testDeleteAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = authDAO.createAuth(usernameOne);

        String usernameTwo = "yourUsername";
        String testAuthTokenTwo = authDAO.createAuth(usernameTwo);

        assertNotNull(authDAO.getAuth(testAuthTokenOne));

        authDAO.deleteAuth(testAuthTokenOne);

        assertNull(authDAO.getAuth(testAuthTokenOne));
    }

    @Test
    public void testClear() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = authDAO.createAuth(usernameOne);

        String usernameTwo = "yourUsername";
        String testAuthTokenTwo = authDAO.createAuth(usernameTwo);

        authDAO.clear();

        assertNull(authDAO.getAuth(testAuthTokenOne));
        assertNull(authDAO.getAuth(testAuthTokenTwo));
    }
}