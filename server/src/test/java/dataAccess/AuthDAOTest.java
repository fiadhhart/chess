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
    public void testInsertAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.insertAuth(usernameOne);

        assertNotNull(testAuthTokenOne);
        assertEquals(usernameOne, AuthDAO.selectAuthUsername(testAuthTokenOne));

    }

    @Test
    public void testSelectAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.insertAuth(usernameOne);
        String test = AuthDAO.selectAuth(testAuthTokenOne);

        assertNotNull(AuthDAO.selectAuth(testAuthTokenOne));
        assertEquals(usernameOne, AuthDAO.selectAuthUsername(testAuthTokenOne));
    }

    @Test
    public void testSelectAuthUsername() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.insertAuth(usernameOne);
        String test = AuthDAO.selectAuthUsername(testAuthTokenOne);

        assertEquals(usernameOne, AuthDAO.selectAuthUsername(testAuthTokenOne));

    }

    @Test
    public void testRemoveAuth() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.insertAuth(usernameOne);

        String usernameTwo = "yourUsername";
        String testAuthTokenTwo = AuthDAO.insertAuth(usernameTwo);

        assertNotNull(AuthDAO.selectAuth(testAuthTokenOne));

        AuthDAO.removeAuth(testAuthTokenOne);

        assertNull(AuthDAO.selectAuth(testAuthTokenOne));

    }

    @Test
    public void testClearAuths() throws DataAccessException {
        String usernameOne = "myUsername";
        String testAuthTokenOne = AuthDAO.insertAuth(usernameOne);

        String usernameTwo = "yourUsername";
        String testAuthTokenTwo = AuthDAO.insertAuth(usernameTwo);

        AuthDAO.clearAuths();

        assertNull(AuthDAO.selectAuth(testAuthTokenOne));
        assertNull(AuthDAO.selectAuth(testAuthTokenTwo));

    }

}