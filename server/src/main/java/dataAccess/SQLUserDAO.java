package dataAccess;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException{
        DatabaseManager.initializeDatabaseAndTables();
    }


    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public String getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String[] getUser(String username, String password) throws DataAccessException {
        return new String[0];
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
