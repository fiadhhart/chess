package dataAccess;

public interface UserDAO {
    void createUser(String username, String password, String email) throws DataAccessException;
    String getUser(String username) throws DataAccessException;
    String[] getUser(String username, String password) throws DataAccessException;
    void clear () throws DataAccessException;
}