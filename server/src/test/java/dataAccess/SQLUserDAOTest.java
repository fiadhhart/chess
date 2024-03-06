package dataAccess;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    private static UserDAO userDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @AfterAll
    public static void cleanUp() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    public void testCreateUser_Positive() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        userDAO.createUser(username, password, email);

        assertNotNull(userDAO.getUser(username));
    }

    @Test
    public void testCreateUser_Negative() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        userDAO.createUser(username, password, email);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(username, password, email));
    }

    @Test
    public void testGetUser_username_Positive() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        userDAO.createUser(username, password, email);

        String foundUsername = userDAO.getUser(username);

        assertEquals(username, foundUsername);
    }

    @Test
    public void testGetUser_username_Negative() throws DataAccessException {
        String username = "nonExistingUser";

        assertNull(userDAO.getUser(username));
    }

    @Test
    public void testGetUser_usernameAndPassword_Positive() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        userDAO.createUser(username, password, email);

        String[] userData = userDAO.getUser(username, password);

        assertNotNull(userData);
        assertEquals(username, userData[0]);
        assertEquals(password, userData[1]);
    }

    @Test
    public void testGetUser_usernameAndPassword_Negative_WrongPassword() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        userDAO.createUser(username, password, email);

        assertNull(userDAO.getUser(username, "wrongPassword"));
    }

    @Test
    public void testGetUser_usernameAndPassword_Negative_NonExistingUser() throws DataAccessException {
        String username = "nonExistingUser";
        String password = "testPassword";

        assertNull(userDAO.getUser(username, password));
    }

    @Test
    public void testClear_Positive() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";
        userDAO.createUser(username, password, email);

        userDAO.clear();

        assertNull(userDAO.getUser(username));
    }

}