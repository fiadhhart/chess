package dataAccess;
import model.AuthData;

import java.util.UUID;

public class AuthDAO {

    //createAuth(username)
    //insert username, authToken into auth
    //returns authToken
    //insertAuth
    public static String createAuth(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString();

        AuthData auth = new AuthData(authToken, username);
        Database.auths.put(authToken, auth);

        return authToken;
    }

    //getAuth(authToken)
    //select authToken from auth
    //return authToken or null
    //selectAuth
    public static String getAuth(String authToken) throws DataAccessException{
        if (Database.auths.containsKey(authToken)) {
            return authToken;
        }
        return null;
    }

    //getUsername(authToken)
    //select username from auth
    //return username
    //selectAuthUsername
    public static String getUsername(String authToken) throws DataAccessException{
        if (Database.auths.containsKey(authToken)) {
            return Database.auths.get(authToken).getUsername();
        }
        return null;
    }

    //deleteAuth(authToken)
    //remove username, authToken from auth
    //no return
    //removeAuth
    public static void deleteAuth (String authToken) throws DataAccessException{
        Database.auths.remove(authToken);
    }

    //clear()
    //remove all from auth
    //no return
    //clearAuths
    public static void clear () throws DataAccessException{
        Database.auths.clear();
    }
}
