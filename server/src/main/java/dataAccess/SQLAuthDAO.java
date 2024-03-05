package dataAccess;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException{
        DatabaseManager.initializeDatabaseAndTables();
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
