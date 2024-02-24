package dataAccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private Database databaseTest;
    private UserDAO userDAO;


    @BeforeEach
    void setUp() {
        databaseTest = new Database();
        databaseTest.clearDatabase();
        userDAO = new UserMemDAO();
    }


    @Test
    public void testCreateUser() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";

        userDAO.createUser(usernameOne, passwordOne, emailOne);

        assertTrue(databaseTest.users.containsKey(usernameOne));
        assertNotNull(databaseTest.users.get(usernameOne));
        assertEquals(usernameOne, databaseTest.users.get(usernameOne).getUsername());
        assertEquals(passwordOne, databaseTest.users.get(usernameOne).getPassword());
        assertEquals(emailOne, databaseTest.users.get(usernameOne).getEmail());
    }

    @Test
    public void testGetUser_username() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        userDAO.createUser(usernameOne, passwordOne, emailOne);

        String testUsernameOne = userDAO.getUser(usernameOne);
        String testNullUsername = userDAO.getUser("DNE");

        assertEquals(usernameOne, testUsernameOne);
        assertNull(testNullUsername);
    }

    @Test
    public void testGetUser_usernameAndPassword() throws DataAccessException {
        String usernameOne = "myUsername";
        String passwordOne = "myPassword";
        String emailOne = "myEmail";
        userDAO.createUser(usernameOne, passwordOne, emailOne);

        String[] testUsernameOne = userDAO.getUser(usernameOne, passwordOne);
        String[] testNullUsername = userDAO.getUser("DNE", "alsoDNE");
        String[] testUsernameTwo = userDAO.getUser(usernameOne, "wrongPassword");

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
        userDAO.createUser(usernameOne, passwordOne, emailOne);

        String usernameTwo = "yourUsername";
        String passwordTwo = "yourPassword";
        String emailTwo = "yourEmail";
        userDAO.createUser(usernameTwo, passwordTwo, emailTwo);

        userDAO.clear();

        assertTrue(databaseTest.users.isEmpty());
    }
}