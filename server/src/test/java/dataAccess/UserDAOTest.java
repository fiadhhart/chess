package dataAccess;

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

        UserMemDAO.createUser(usernameOne, passwordOne, emailOne);

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
        UserMemDAO.createUser(usernameOne, passwordOne, emailOne);

        String testUsernameOne = UserMemDAO.getUser(usernameOne);
        String testNullUsername = UserMemDAO.getUser("DNE");

        assertEquals(usernameOne, testUsernameOne);
        assertNull(testNullUsername);
    }

    @Test
    public void testGetUser_usernameAndPassword() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserMemDAO.createUser(usernameOne, passwordOne, emailOne);

        String[] testUsernameOne = UserMemDAO.getUser(usernameOne, passwordOne);
        String[] testNullUsername = UserMemDAO.getUser("DNE", "alsoDNE");
        String[] testUsernameTwo = UserMemDAO.getUser(usernameOne, "wrongPassword");

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
        UserMemDAO.createUser(usernameOne, passwordOne, emailOne);

        String usernameTwo = "yourUsername";
        String passwordTwo = "yourPassword";
        String emailTwo = "yourEmail";
        UserMemDAO.createUser(usernameTwo, passwordTwo, emailTwo);

        UserMemDAO.clear();

        assertTrue(Database.users.isEmpty());
    }

}