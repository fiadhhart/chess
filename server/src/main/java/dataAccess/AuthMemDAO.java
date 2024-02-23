package dataAccess;
import model.AuthData;

import java.util.UUID;

public class AuthMemDAO implements AuthDAO{

    private Database database = new Database();

    //createAuth(username)
    //insert username, authToken into auth
    //returns authToken
    //insertAuth
    @Override
    public String createAuth(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString();

        AuthData auth = new AuthData(authToken, username);
        database.auths.put(authToken, auth);

        return authToken;
    }

    //getAuth(authToken)
    //select authToken from auth
    //return authToken or null
    //selectAuth
    @Override
    public String getAuth(String authToken) throws DataAccessException{
        if (database.auths.containsKey(authToken)) {
            return authToken;
        }
        return null;
    }

    //getUsername(authToken)
    //select username from auth
    //return username
    //selectAuthUsername
    @Override
    public String getUsername(String authToken) throws DataAccessException{
        if (database.auths.containsKey(authToken)) {
            return database.auths.get(authToken).getUsername();
        }
        return null;
    }

    //deleteAuth(authToken)
    //remove username, authToken from auth
    //no return
    //removeAuth
    @Override
    public void deleteAuth (String authToken) throws DataAccessException{
        database.auths.remove(authToken);
    }

    //clear()
    //remove all from auth
    //no return
    //clearAuths
    @Override
    public void clear () throws DataAccessException{
        database.auths.clear();
    }
}
