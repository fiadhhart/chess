package dataAccess;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @AfterAll
    public static void cleanUp() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
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

        //create same user twice
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
        String wrongUsername = "wrongUser";

        //user DNE
        assertNull(userDAO.getUser(wrongUsername));
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
    public void testGetUser_usernameAndPassword_Negative() throws DataAccessException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";
        String wrongPassword = "wrongPassword";
        String wrongUsername = "wrongUser";

        userDAO.createUser(username, password, email);

        //wrong password
        assertNull(userDAO.getUser(username, wrongPassword));

        //user DNE
        assertNull(userDAO.getUser(wrongUsername, password));
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