package dataAccess;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
        database.clearDatabase();
    }

    @Test
    public void testCreateUser() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";

        UserDAO.createUser(usernameOne, passwordOne, emailOne);

        assertTrue(Database.users.containsKey(usernameOne));
        assertNotNull(Database.users.get(usernameOne));
        assertEquals(usernameOne, Database.users.get(usernameOne).getUsername());
        assertEquals(passwordOne, Database.users.get(usernameOne).getPassword());
        assertEquals(emailOne, Database.users.get(usernameOne).getEmail());
    }

    @Test
    public void testGetUser_username() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserDAO.createUser(usernameOne, passwordOne, emailOne);

        String testUsernameOne = UserDAO.getUser(usernameOne);
        String testNullUsername = UserDAO.getUser("DNE");

        assertEquals(usernameOne, testUsernameOne);
        assertNull(testNullUsername);
    }

    @Test
    public void testGetUser_usernameAndPassword() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserDAO.createUser(usernameOne, passwordOne, emailOne);

        String[] testUsernameOne = UserDAO.getUser(usernameOne, passwordOne);
        String[] testNullUsername = UserDAO.getUser("DNE", "alsoDNE");
        String[] testUsernameTwo = UserDAO.getUser(usernameOne, "wrongPassword");

        assertNotNull(testUsernameOne);
        assertEquals(usernameOne, testUsernameOne[0]);
        assertEquals(passwordOne, testUsernameOne[1]);
        assertNull(testNullUsername);
        assertNull(testUsernameTwo);
    }

    @Test
    public void testClear() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserDAO.createUser(usernameOne, passwordOne, emailOne);

        String usernameTwo = "yourUsername";
        String passwordTwo = "yourPassword";
        String emailTwo = "yourEmail";
        UserDAO.createUser(usernameTwo, passwordTwo, emailTwo);

        UserDAO.clear();

        assertTrue(Database.users.isEmpty());
    }

}