package dataAccess.memory;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class AuthMemDAO implements AuthDAO {
    private Database database;

    public AuthMemDAO(Database database) {
        this.database = database;
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        while (database.auths.containsKey(authToken)) {
            authToken = UUID.randomUUID().toString();
        }

        AuthData auth = new AuthData(authToken, username);
        database.auths.put(authToken, auth);

        return authToken;
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException{
        if (database.auths.containsKey(authToken)) {
            return authToken;
        }
        return null;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException{
        if (database.auths.containsKey(authToken)) {
            return database.auths.get(authToken).getUsername();
        }
        return null;
    }

    @Override
    public void deleteAuth (String authToken) throws DataAccessException{
        database.auths.remove(authToken);
    }

    @Override
    public void clear () throws DataAccessException{
        database.auths.clear();
    }
}