package dataAccess;

public interface UserDAO {

    void createUser(String username, String password, String email) throws DataAccessException;
}
