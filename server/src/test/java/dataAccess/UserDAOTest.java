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
    public void testInsertUser() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";

        UserDAO.insertUser(usernameOne, passwordOne, emailOne);

        assertTrue(Database.users.containsKey(usernameOne));
        assertNotNull(Database.users.get(usernameOne));
        assertEquals(usernameOne, Database.users.get(usernameOne).getUsername());
        assertEquals(passwordOne, Database.users.get(usernameOne).getPassword());
        assertEquals(emailOne, Database.users.get(usernameOne).getEmail());
    }

    @Test
    public void testSelectUser_username() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserDAO.insertUser(usernameOne, passwordOne, emailOne);

        String testUsernameOne = UserDAO.selectUser(usernameOne);
        String testNullUsername = UserDAO.selectUser("DNE");

        assertEquals(usernameOne, testUsernameOne);
        assertNull(testNullUsername);
    }

    @Test
    public void testSelectUser_usernameAndPassword() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserDAO.insertUser(usernameOne, passwordOne, emailOne);

        String[] testUsernameOne = UserDAO.selectUser(usernameOne, passwordOne);
        String[] testNullUsername = UserDAO.selectUser("DNE", "alsoDNE");
        String[] testUsernameTwo = UserDAO.selectUser(usernameOne, "wrongPassword");

        assertNotNull(testUsernameOne);
        assertEquals(usernameOne, testUsernameOne[0]);
        assertEquals(passwordOne, testUsernameOne[1]);
        assertNull(testNullUsername);
        assertNull(testUsernameTwo);
    }

    @Test
    public void testClearUsers() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        UserDAO.insertUser(usernameOne, passwordOne, emailOne);

        String usernameTwo = "yourUsername";
        String passwordTwo = "yourPassword";
        String emailTwo = "yourEmail";
        UserDAO.insertUser(usernameTwo, passwordTwo, emailTwo);

        UserDAO.clearUsers();

        assertTrue(Database.users.isEmpty());
    }

}